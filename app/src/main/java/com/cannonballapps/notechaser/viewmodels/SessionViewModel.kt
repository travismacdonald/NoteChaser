package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.data.QuestionLog
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.SoundEffectPlayer
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessor
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessorListener
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessor
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessorListener
import com.cannonballapps.notechaser.musicutilities.*
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.playablegenerator.PlayableFactory
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.first
import timber.log.Timber
import kotlin.properties.Delegates


const val COUNTDOWN_SECONDS = 3

// TODO: consistent variable names
@ObsoleteCoroutinesApi
class SessionViewModel @ViewModelInject constructor(
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
    lateinit var sessionType: SessionType
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
            answersShouldMatchOctave = prefsStore.matchOctave().first()

            sessionType = prefsStore.sessionType().first()
            if (sessionType == SessionType.TIME_LIMIT) {
                sessionTimeLenInMinutes = prefsStore.sessionTimeLimit().first()
            }
            else if (sessionType == SessionType.QUESTION_LIMIT) {
                numQuestions = prefsStore.numQuestions().first()
            }

            shouldPlayReferencePitchOnStart = prefsStore.playStartingPitch().first()
            if (shouldPlayReferencePitchOnStart) {
                referencePitch = makeStartingPitch()
            }

        }
        setupPitchProcessingCallbacks()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared called")
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
                Timber.d("should play starting pitch")
                _sessionState.value = State.PLAYING_STARTING_PITCH
                val refPitchPlayable = PlayableFactory.makePlayableFromNote(referencePitch!!)
                playableJob = launchPlayPlayable(refPitchPlayable)
                playableJob?.join()
                delay(timeMillis = 1000)
            }

            // TODO: refactor function: initSessionVariables
            sessionTimerJob = beginSessionTimer()
            _numQuestionsCorrect.value = 0
            _elapsedSessionTimeInSeconds.value = 0
            _numQuestionsSkipped.value = 0
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
        Timber.d("endSession called")
        if (_sessionState.value == State.COUNTDOWN) {
            Timber.d("cancelling countdown job")
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
        }
        else if (_sessionState.value == State.LISTENING) {
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
        }
        else {
            startNextCycle(millisInBetweenQuestions)
        }
    }

    fun replayQuestion() {
        // TODO
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
            val notePoolType = prefsStore.notePoolType().first()
            val key = prefsStore.questionKey().first()
            val lowerBound = prefsStore.playableLowerBound().first()
            val upperBound = prefsStore.playableUpperBound().first()

            if (notePoolType == NotePoolType.CHROMATIC) {
                val degrees = prefsStore.chromaticDegrees().first()
                generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromChromaticDegrees(
                        degrees,
                        key,
                        lowerBound,
                        upperBound
                )
            }
            else if (notePoolType == NotePoolType.DIATONIC) {
                val degrees = prefsStore.diatonicDegrees().first()
                val parentScale = prefsStore.parentScale().first()
                val modeIx = prefsStore.modeIx().first()
                val scale = parentScale.getModeAtIx(modeIx)
                generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromDiatonicDegrees(
                        degrees,
                        scale,
                        key,
                        lowerBound,
                        upperBound
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
                val curNote = NoteFactory.makeNoteFromMidiNumber(note)
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
        }
        else {
            startNextCycle(millisInBetweenQuestions)
        }
    }

    private fun sessionIsComplete(): Boolean {
        return isTimedSession() && timeLimitReached() || isQuestionLimitSession() && questionLimitReached()
    }

    private fun isTimedSession() =
            sessionType == SessionType.TIME_LIMIT

    private fun timeLimitReached() =
            _elapsedSessionTimeInSeconds.value!! >= sessionTimeLenInMinutes * 60

    private fun isQuestionLimitSession() =
            sessionType == SessionType.QUESTION_LIMIT

    private fun questionLimitReached() =
            _numQuestionsCorrect.value!! == numQuestions

    @ObsoleteCoroutinesApi
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
                skipped
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
        return (answersShouldMatchOctave && isSamePatternSameOctave(actualAnswer, userAnswer) ||
                !answersShouldMatchOctave && isSamePatternAnyOctave(actualAnswer, userAnswer))
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
        val key = prefsStore.questionKey().first()
        val keyTransposed = key.value + (MusicTheoryUtils.OCTAVE_SIZE * 5)
        return NoteFactory.makeNoteFromMidiNumber(keyTransposed)
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
