package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.data.QuestionLog
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessor
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessorListener
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessor
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessorListener
import com.cannonballapps.notechaser.musicutilities.*
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.first
import timber.log.Timber
import kotlin.properties.Delegates


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

    private val _numCorrectAnswers = MutableLiveData(0)
    val numCorrectAnswers: LiveData<Int>
        get() = _numCorrectAnswers

    private val _numRepeatsForCurrentQuestion = MutableLiveData(0)
    val numRepeatsForCurrentQuestion: LiveData<Int>
        get() = _numRepeatsForCurrentQuestion

    private val _sessionState = MutableLiveData(State.INACTIVE)
    val sessionState: LiveData<State>
        get() = _sessionState

    // This var ONLY records time spent answering question. It does not record the time
    // in which the question is being played.
    private val _timeSpentAnsweringCurrentQuestionInMillis = MutableLiveData(0L)
    val timeSpentAnsweringCurrentQuestionInMillis: LiveData<Long>
        get() = _timeSpentAnsweringCurrentQuestionInMillis

    // Elapsed time records time from start of session to end
    private val _elapsedSessionTimeInSeconds = MutableLiveData(0)
    val sessionTimeInSeconds: LiveData<Int>
        get() = _elapsedSessionTimeInSeconds

    private var answersShouldMatchOctave by Delegates.notNull<Boolean>()
    private val millisInBetweenQuestions = 350L
    private val silenceThreshold = 2500L

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer
    private var pitchProcessor = SignalProcessor()
    private var noteProcessor = NoteProcessor()

    private var sessionJob: Job? = null
    private var playableJob: Job? = null
    private var processorJob: Job? = null

    private var sessionTimerJob: Job? = null
    private var currentQuestionTimerJob: Job? = null
    private var repeatPlayableJob: Job? = null

    private val questionLogs: MutableList<QuestionLog> = arrayListOf()

    init {
        viewModelScope.launch {
            answersShouldMatchOctave = prefsStore.matchOctave().first()
        }
        setupPitchProcessingCallbacks()

    }

    override fun onCleared() {
        super.onCleared()
        playablePlayer.midiPlayer.stop()
    }

    fun startSession() {
        assertSessionNotStarted()

        if (this::playablePlayer.isInitialized) {
            sessionTimerJob = beginSessionTimer()
            startNextCycle()
        }
        else {
            TODO("wait and try again?")
        }
    }

    fun endSession() {
        if (sessionJob == null) {
            return
        }
        sessionJob?.cancel()
        sessionJob = null

        if (_sessionState.value == State.PLAYING) {
            cancelPlayableJob()
        }
        else if (_sessionState.value == State.LISTENING) {
            cancelProcessorJob()
        }
        _sessionState.value = State.INACTIVE
    }

    fun initGenerator(type: ExerciseType) {
        viewModelScope.launch {
            makePlayableGenerator(type)
        }
    }

    private fun launchSessionCycle(playable: Playable, delayBeforeStarting: Long = -1): Job {
        return viewModelScope.launch {
            if (delayBeforeStarting != -1L) {
                _sessionState.value = State.WAITING
                delay(delayBeforeStarting)
            }
            playableJob = launchPlayPlayable(playable).also { job ->
                job.join()
            }
            currentQuestionTimerJob = timeUserAnswer()
            repeatPlayableJob = launchRepeatPlayableJob()
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
            _sessionState.value = State.PLAYING
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
                repeatPlayableJob?.cancel()
                val curNote = NoteFactory.makeNoteFromMidiNumber(note)
                _curFilteredNoteDetected.value = curNote
                addNoteToUserAnswer(curNote)
                if (userAnswerIsCorrect()) {
                    onAnswerCorrect()
                }
            }

            override fun notifyNoteUndetected(note: Int) {
                _curFilteredNoteDetected.value = null
                repeatPlayableJob = launchRepeatPlayableJob()
            }
        }
    }

    // TODO: handle whether it's a timed session or a num questions session
    @ObsoleteCoroutinesApi
    private fun onAnswerCorrect() {
        cancelProcessorJob()
        currentQuestionTimerJob?.cancel()
        questionLogs.add(makeLogForCurrentQuestion())
        _numCorrectAnswers.value = _numCorrectAnswers.value!!.plus(1)
        if (_numCorrectAnswers.value != 10000000) {
            startNextCycle()
        }
        else {
            TODO("go to statistics fragment")
        }
    }

    @ObsoleteCoroutinesApi
    private fun onSilenceThresholdMet() {
        cancelProcessorJob()
        currentQuestionTimerJob?.cancel()
        val playable = _curPlayable.value!!
        sessionJob = launchSessionCycle(playable)
    }

    private fun assertSessionNotStarted() {
        if (sessionJob != null) {
            throw IllegalArgumentException("startSession() called before endSession()")
        }
    }

    private fun makeLogForCurrentQuestion(): QuestionLog {
        return QuestionLog(
                _curPlayable.value!!,
                _timeSpentAnsweringCurrentQuestionInMillis.value!!,
                _numRepeatsForCurrentQuestion.value!!
        )
    }

    private fun clearLocalSessionState() {
        noteProcessor.clear()
        _curFilteredNoteDetected.value = null
        _curPitchDetectedAsMidiNumber.value = null
        _userAnswer.value = arrayListOf()
        _timeSpentAnsweringCurrentQuestionInMillis.value = 0
        _numRepeatsForCurrentQuestion.value = 0
//        _curPlayable.value = null
    }

    @ObsoleteCoroutinesApi
    private fun startNextCycle() {
        clearLocalSessionState()
        val playable = getNextPlayable()
        sessionJob = launchSessionCycle(playable, millisInBetweenQuestions)
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
                Timber.d("just ticked!")
            }
        }
    }

    enum class State {
        LISTENING,
        PLAYING,
        WAITING,
        INACTIVE,
    }

}
