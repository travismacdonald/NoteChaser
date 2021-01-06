package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
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
import kotlinx.coroutines.flow.first
import timber.log.Timber
import kotlin.properties.Delegates


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

    private val _sessionState = MutableLiveData(State.INACTIVE)
    val sessionState: LiveData<State>
        get() = _sessionState

    private var answersShouldMatchOctave by Delegates.notNull<Boolean>()

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer
    private var pitchProcessor = SignalProcessor()
    private var noteProcessor = NoteProcessor()

    private var sessionJob: Job? = null
    private var playableJob: Job? = null
    private var processorJob: Job? = null

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
            sessionJob = launchSession()
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

    private fun launchSession(): Job {
        return viewModelScope.launch {

            val nextPlayable = generator.generatePlayable()
            _curPlayable.value = nextPlayable

            playableJob = launchPlayPlayable(nextPlayable)
            playableJob?.join()

            processorJob = launchAnswerProcessing().also {
                it.join()
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

    private fun cancelPlayableJob() {
        playableJob?.cancel()
        playablePlayer.stopCurPlayable()
    }

    private fun cancelProcessorJob() {
        pitchProcessor.stop()

        _curPitchDetectedAsMidiNumber.value = -1
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
                val curNote = NoteFactory.makeNoteFromMidiNumber(note)
                _curFilteredNoteDetected.value = curNote
                addNoteToUserAnswer(curNote)
                if (userAnswerIsCorrect()) {
                    _numCorrectAnswers.value = _numCorrectAnswers.value!!.plus(1)
                }
            }

            override fun notifyNoteUndetected(note: Int) {
                _curFilteredNoteDetected.value = null
            }
        }
    }

    private fun assertSessionNotStarted() {
        if (sessionJob != null) {
            throw IllegalArgumentException("startSession() called before endSession()")
        }
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

    enum class State {
        LISTENING,
        PLAYING,
        INACTIVE,
    }

}

