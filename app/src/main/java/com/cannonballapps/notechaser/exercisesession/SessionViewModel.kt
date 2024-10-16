package com.cannonballapps.notechaser.exercisesession

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseType
import com.cannonballapps.notechaser.common.NotePoolType
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.QuestionLog
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.common.SoundEffectPlayer
import com.cannonballapps.notechaser.common.noteprocessor.NoteProcessor
import com.cannonballapps.notechaser.common.noteprocessor.NoteProcessorListener
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.common.signalprocessor.SignalProcessor
import com.cannonballapps.notechaser.common.signalprocessor.SignalProcessorListener
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableFactory
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGeneratorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.properties.Delegates

const val COUNTDOWN_SECONDS = 3

// TODO: consistent variable names
@ObsoleteCoroutinesApi
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    private val _curPitchDetectedAsMidiNumber = MutableLiveData<Int?>(null)
    val curPitchDetectedAsMidiNumber: LiveData<Int?>
        get() = _curPitchDetectedAsMidiNumber

    private val _curFilteredNoteDetected = MutableLiveData<Note?>(null)
    val curFilteredNoteDetected: LiveData<Note?>
        get() = _curFilteredNoteDetected

    private val _curPlayable = MutableLiveData<Playable?>(null)
    val curPlayable: LiveData<Playable?>
        get() = _curPlayable

    private val _userAnswer = MutableLiveData<ArrayList<Note>>(arrayListOf())
    val userAnswer: LiveData<out List<Note>>
        get() = _userAnswer

    private val _numQuestionsCorrect = MutableLiveData<Int>()
    val numQuestionsCorrect: LiveData<Int>
        get() = _numQuestionsCorrect

    private val _numQuestionsSkipped = MutableLiveData<Int>()
    val numQuestionsSkipped: LiveData<Int>
        get() = _numQuestionsSkipped

    private val _numQuestionsCorrectOrSkipped = MediatorLiveData<Int>().apply {
        addSource(_numQuestionsSkipped) { numSkipped ->
            _numQuestionsCorrect.value?.let { numCorrect ->
                this.value = numSkipped + numCorrect
            }
        }
        addSource(_numQuestionsCorrect) { numCorrect ->
            _numQuestionsSkipped.value?.let { numSkipped ->
                this.value = numSkipped + numCorrect
            }
        }
    }
    val numQuestionsCorrectOrSkipped: LiveData<Int>
        get() = _numQuestionsCorrectOrSkipped

    private val _numRepeatsForCurrentQuestion = MutableLiveData(0)
    val numRepeatsForCurrentQuestion: LiveData<Int>
        get() = _numRepeatsForCurrentQuestion

    private val _secondsUntilSessionStart = MutableLiveData(0)
    val secondsUntilSessionStart: LiveData<Int>
        get() = _secondsUntilSessionStart

    private val _sessionState = MutableLiveData(State.INACTIVE)
    val sessionState: LiveData<State>
        get() = _sessionState

    // This var ONLY records time spent answering question. It does not record the time
    // in which the question is being played.
    private val _timeSpentAnsweringCurrentQuestionInMillis = MutableLiveData(0L)
    val timeSpentAnsweringCurrentQuestionInMillis: LiveData<Long>
        get() = _timeSpentAnsweringCurrentQuestionInMillis

    // Elapsed time records time from start of session to end
    private val _elapsedSessionTimeInSeconds = MutableLiveData<Int>()
    val elapsedSessionTimeInSeconds: LiveData<Int>
        get() = _elapsedSessionTimeInSeconds

    private var answersShouldMatchOctave by Delegates.notNull<Boolean>()
    var numQuestions by Delegates.notNull<Int>()
    var sessionTimeLenInMinutes by Delegates.notNull<Int>()
    lateinit var sessionLengthSettings: SessionLengthSettings
    var sessionHasStarted = false
        private set

    private val millisInBetweenQuestions = 450L
    private val silenceThreshold = 2750L

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer
    private var pitchProcessor = SignalProcessor()
    private var noteProcessor = NoteProcessor()
    lateinit var soundEffectPlayer: SoundEffectPlayer

    private var sessionJob: Job? = null

    private var countDownJob: Job? = null
    private var referencePitchJob: Job? = null
    private var playableJob: Job? = null
    private var processorJob: Job? = null

    private var silenceThresholdJob: Job? = null
    private var sessionTimerJob: Job? = null
    private var currentQuestionTimerJob: Job? = null

    private var shouldPlayReferencePitchOnStart by Delegates.notNull<Boolean>()
    var referencePitch: Note? = null

    private val questionLogs: MutableList<QuestionLog> = arrayListOf()

    init {
        viewModelScope.launch {
            fetchPrefStoreData()
        }
        setupPitchProcessingCallbacks()
    }

    override fun onCleared() {
        super.onCleared()
        playablePlayer.midiPlayer.stop()
        endSession()
    }

    fun startSession() {
        assertSessionNotStarted()
        sessionHasStarted = true
        countDownJob = viewModelScope.launch {
            _sessionState.value = State.COUNTDOWN
            _secondsUntilSessionStart.value = COUNTDOWN_SECONDS
            repeat(COUNTDOWN_SECONDS) {
                delay(timeMillis = 1000)
                if (isActive) {
                    _secondsUntilSessionStart.value = _secondsUntilSessionStart.value!!.minus(1)
                }
            }

            if (shouldPlayReferencePitchOnStart) {
                _sessionState.value = State.PLAYING_STARTING_PITCH
                val refPitchPlayable = PlayableFactory.makePlayableFromNote(referencePitch!!)
                playableJob = launchPlayPlayable(refPitchPlayable)
                playableJob?.join()
                delay(timeMillis = 1000)
            }

            // TODO: refactor function: initSessionVariables
            sessionTimerJob = beginSessionTimer()
            initSessionVariables()
            startNextCycle(-1)
        }
    }

    fun resumeSession() {
        // TODO
    }

    fun pauseSession() {
        // TODO
    }

    // TODO: this function could use some cleaning up
    fun endSession() {
        if (_sessionState.value == State.COUNTDOWN) {
            countDownJob?.cancel()
            _sessionState.value = State.INACTIVE
            return
        }
        if (sessionJob == null) {
            return
        }
        currentQuestionTimerJob?.cancel()
        silenceThresholdJob?.cancel()
        sessionJob?.cancel()
        sessionJob = null
        sessionTimerJob?.cancel()

        if (_sessionState.value == State.PLAYING_QUESTION) {
            cancelPlayableJob()
        } else if (_sessionState.value == State.LISTENING) {
            cancelProcessorJob()
        }
        _sessionState.value = State.INACTIVE
    }

    fun skipQuestion() {
        silenceThresholdJob?.cancel()
        cancelProcessorJob()
        _sessionState.value = State.QUESTION_SKIPPED
        questionLogs.add(makeLogForCurrentQuestion(skipped = true))
        _numQuestionsSkipped.value = _numQuestionsSkipped.value!!.plus(1)

        if (isQuestionLimitSession() && questionLimitReached()) {
            onSessionCompleted()
        } else {
            startNextCycle(millisInBetweenQuestions)
        }
    }

    fun replayQuestion() {
        silenceThresholdJob?.cancel()
        cancelProcessorJob()
        currentQuestionTimerJob?.cancel()
        val playable = _curPlayable.value!!
        sessionJob = launchSessionCycle(playable)
    }

    fun initGenerator(type: ExerciseType) {
        viewModelScope.launch {
            makePlayableGenerator(type)
        }
    }

    private fun launchSessionCycle(playable: Playable, delayBeforeStarting: Long = -1): Job {
        return viewModelScope.launch {
            if (delayBeforeStarting != -1L) {
//                _sessionState.value = State.WAITING
                delay(delayBeforeStarting)
            }

            _sessionState.value = State.PLAYING_QUESTION
            playableJob = launchPlayPlayable(playable).also { job ->
                job.join()
            }
            currentQuestionTimerJob = timeUserAnswer()
            silenceThresholdJob = launchRepeatPlayableJob()
            processorJob = launchAnswerProcessing().also { job ->
                job.join()
            }
        }
    }

    private fun timeUserAnswer(): Job {
        val tickLenInMillis = 100L
        val ticker = ticker(delayMillis = tickLenInMillis)
        return viewModelScope.launch {
            for (tick in ticker) {
                _timeSpentAnsweringCurrentQuestionInMillis.value =
                    _timeSpentAnsweringCurrentQuestionInMillis.value!!.plus(tickLenInMillis)
            }
        }
    }

    private fun launchPlayPlayable(playable: Playable): Job {
        return viewModelScope.launch {
            playablePlayer.playPlayable(playable)
        }
    }

    private fun launchAnswerProcessing(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            runBlocking(Dispatchers.Main) {
                _sessionState.value = State.LISTENING
            }
            noteProcessor.clear()
            pitchProcessor.start()
        }
    }

    private fun launchRepeatPlayableJob(): Job {
        return viewModelScope.launch {
            delay(silenceThreshold)
            onSilenceThresholdMet()
        }
    }

    private fun cancelPlayableJob() {
        playableJob?.cancel()
        playablePlayer.stopCurPlayable()
    }

    private fun cancelProcessorJob() {
        pitchProcessor.stop()
        _curPitchDetectedAsMidiNumber.value = null
    }

    private fun makePlayableGenerator(type: ExerciseType) {
        when (type) {
            // TODO: will implement other types later
            ExerciseType.SINGLE_NOTE -> makeNotePlayableGenerator()

            else -> throw IllegalArgumentException("Unrecognized ExerciseType given: $type")
        }
    }

    // TODO: could clean a bit
    private fun makeNotePlayableGenerator() {
        viewModelScope.launch {
            val notePoolType = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.notePoolType
            val key = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.questionKey
            val lowerBound = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.playableBounds.lower
            val upperBound = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.playableBounds.upper

            if (notePoolType is NotePoolType.Chromatic) {
                generator = PlayableGeneratorFactory().makeNotePlayableGeneratorFromChromaticDegrees(
                    notePoolType.degrees,
                    key,
                    lowerBound,
                    upperBound,
                )
            } else if (notePoolType is NotePoolType.Diatonic) {
                generator = PlayableGeneratorFactory().makeNotePlayableGeneratorFromDiatonicDegrees(
                    notePoolType.degrees,
                    notePoolType.scale,
                    key,
                    lowerBound,
                    upperBound,
                )
            }
        }
    }

    private fun setupPitchProcessingCallbacks() {
        pitchProcessor.listener = object : SignalProcessorListener {
            override fun notifyPitchResult(pitchAsMidiNumber: Int?, probability: Float, isPitched: Boolean) {
                runBlocking(Dispatchers.Main) {
                    _curPitchDetectedAsMidiNumber.value = pitchAsMidiNumber
                    noteProcessor.onPitchDetected(pitchAsMidiNumber)
                }
            }
        }

        noteProcessor.listener = object : NoteProcessorListener {
            override fun notifyNoteDetected(note: Int) {
                silenceThresholdJob?.cancel()
                val curNote = Note(note)
                _curFilteredNoteDetected.value = curNote
                addNoteToUserAnswer(curNote)
                if (userAnswerIsCorrect()) {
                    onAnswerCorrect()
                }
            }

            override fun notifyNoteUndetected(note: Int) {
                _curFilteredNoteDetected.value = null
                silenceThresholdJob = launchRepeatPlayableJob()
            }
        }
    }

    // TODO: handle whether it's a timed session or a num questions session
    @ObsoleteCoroutinesApi
    private fun onAnswerCorrect() {
        cancelProcessorJob()
        currentQuestionTimerJob?.cancel()
        _sessionState.value = State.ANSWER_CORRECT
        soundEffectPlayer.playCorrectSound()
        questionLogs.add(makeLogForCurrentQuestion(skipped = false))
        _numQuestionsCorrect.value = _numQuestionsCorrect.value!!.plus(1)
        if (isQuestionLimitSession() && questionLimitReached()) {
            onSessionCompleted()
        } else {
            startNextCycle(millisInBetweenQuestions)
        }
    }

    private fun sessionIsComplete(): Boolean {
        return isTimedSession() && timeLimitReached() || isQuestionLimitSession() && questionLimitReached()
    }

    private fun isTimedSession() =
        sessionLengthSettings is SessionLengthSettings.TimeLimit

    private fun timeLimitReached() =
        _elapsedSessionTimeInSeconds.value!! >= sessionTimeLenInMinutes * 60

    private fun isQuestionLimitSession() =
        sessionLengthSettings is SessionLengthSettings.QuestionLimit

    private fun questionLimitReached() =
        _numQuestionsCorrect.value!! == numQuestions

    private fun onSilenceThresholdMet() {
        cancelProcessorJob()
        currentQuestionTimerJob?.cancel()
        val playable = _curPlayable.value!!
        sessionJob = launchSessionCycle(playable)
    }

    private fun assertSessionNotStarted() {
        if (sessionJob != null) {
            throw IllegalStateException("startSession() called before endSession()")
        }
    }

    private fun makeLogForCurrentQuestion(skipped: Boolean): QuestionLog {
        return QuestionLog(
            _curPlayable.value!!,
            _timeSpentAnsweringCurrentQuestionInMillis.value!!,
            _numRepeatsForCurrentQuestion.value!!,
            skipped,
        )
    }

    private fun clearLocalSessionState() {
        noteProcessor.clear()
        _curFilteredNoteDetected.value = null
        _curPitchDetectedAsMidiNumber.value = null
        _userAnswer.value = arrayListOf()
        _timeSpentAnsweringCurrentQuestionInMillis.value = 0
        _numRepeatsForCurrentQuestion.value = 0
        _curPlayable.value = null
    }

    @ObsoleteCoroutinesApi
    private fun startNextCycle(delayBeforeStarting: Long) {
        clearLocalSessionState()
        val playable = getNextPlayable()
        sessionJob = launchSessionCycle(playable, delayBeforeStarting)
    }

    private fun addNoteToUserAnswer(note: Note) {
        val notes = _userAnswer.value!!
        if (notes.size == _curPlayable.value!!.notes.size) {
            notes.removeAt(0)
        }
        notes.add(note)
        _userAnswer.value = notes
    }

    private fun userAnswerIsCorrect(): Boolean {
        val actualAnswer = _curPlayable.value!!.notes
        val userAnswer = _userAnswer.value!!
        return (
            answersShouldMatchOctave && isSamePatternSameOctave(actualAnswer, userAnswer) ||
                !answersShouldMatchOctave && isSamePatternAnyOctave(actualAnswer, userAnswer)
            )
    }

    private fun getNextPlayable(): Playable {
        val nextPlayable = generator.generatePlayable()
        _curPlayable.value = nextPlayable
        return nextPlayable
    }

    private fun isSamePatternSameOctave(lhs: List<Note>, rhs: List<Note>): Boolean {
        return lhs.map { it.midiNumber } == rhs.map { it.midiNumber }
    }

    private fun isSamePatternAnyOctave(lhs: List<Note>, rhs: List<Note>): Boolean {
        return transposeNotesToLowestOctave(lhs) == transposeNotesToLowestOctave(rhs)
    }

    private fun transposeNotesToLowestOctave(notes: List<Note>): List<Int> {
        val lowestNote = notes.minByOrNull { it.midiNumber }!!
        val numOctavesFromZero = lowestNote.midiNumber / MusicTheoryUtils.OCTAVE_SIZE
        val offset = numOctavesFromZero * MusicTheoryUtils.OCTAVE_SIZE
        return notes.map { it.midiNumber - offset }
    }

    @ObsoleteCoroutinesApi
    private fun beginSessionTimer(): Job {
        val ticker = ticker(delayMillis = 1000)
        return viewModelScope.launch {
            _elapsedSessionTimeInSeconds.value = 0
            for (tick in ticker) {
                _elapsedSessionTimeInSeconds.value = _elapsedSessionTimeInSeconds.value!! + 1
                if (isTimedSession() && timeLimitReached()) {
                    onSessionCompleted()
                }
            }
        }
    }

    private fun onSessionCompleted() {
        endSession()
        viewModelScope.launch {
            _sessionState.value = State.FINISHING
            // TODO: clean up hardcoded value
            delay(3000)
            _sessionState.value = State.FINISHED
        }
    }

    private suspend fun makeStartingPitch(): Note {
        val key = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.questionKey
        val keyTransposed = key.value + (MusicTheoryUtils.OCTAVE_SIZE * 5)
        return Note(keyTransposed)
    }

    private suspend fun fetchPrefStoreData() {
        answersShouldMatchOctave = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.shouldMatchOctave

        // todo refactor
        sessionLengthSettings = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionLengthSettings
        (sessionLengthSettings as? SessionLengthSettings.TimeLimit)?.let {
            sessionTimeLenInMinutes = it.timeLimitMinutes
        }
        (sessionLengthSettings as? SessionLengthSettings.QuestionLimit)?.let {
            numQuestions = it.numQuestions
        }

        shouldPlayReferencePitchOnStart = (prefsStore.exerciseSettingsFlow().first() as ResultOf.Success).value.sessionQuestionSettings.shouldPlayStartingPitch
        if (shouldPlayReferencePitchOnStart) {
            referencePitch = makeStartingPitch()
        }
    }

    private fun initSessionVariables() {
        _numQuestionsCorrect.value = 0
        _elapsedSessionTimeInSeconds.value = 0
        _numQuestionsSkipped.value = 0
    }

    enum class State {
        INACTIVE,
        COUNTDOWN,
        PLAYING_STARTING_PITCH,
        PLAYING_QUESTION,
        LISTENING,
        ANSWER_CORRECT,
        QUESTION_SKIPPED,
        FINISHING,
        FINISHED,
    }
}
