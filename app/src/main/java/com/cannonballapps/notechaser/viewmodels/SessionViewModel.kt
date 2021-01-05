package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessor
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessorListener
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.getModeAtIx
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


class SessionViewModel @ViewModelInject constructor(
        private val prefsStore: PrefsStore,
) : ViewModel() {

    private val _curPitchDetectedAsMidiNumber = MutableLiveData<Int>()
    val curPitchDetectedAsMidiNumber: LiveData<Int>
        get() = _curPitchDetectedAsMidiNumber

    private val _curPlayable = MutableLiveData<Playable>()
    val curPlayable: LiveData<Playable>
        get() = _curPlayable

    private val _sessionState = MutableLiveData(State.INACTIVE)
    val sessionState: LiveData<State>
        get() = _sessionState

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer
    private var pitchProcessor = SignalProcessor().also {
        it.listener = SignalProcessorListener { pitch, _, _ ->
            runBlocking(Dispatchers.Main) {
                _curPitchDetectedAsMidiNumber.value = pitch
            }
        }
    }

    private var sessionJob: Job? = null
    private var playableJob: Job? = null
    private var processorJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        playablePlayer.midiPlayer.stop()
    }

    fun startSession() {
        assertSessionNotStarted()

        if (this::playablePlayer.isInitialized) {
            sessionJob = launchSession()
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

            playableJob = launchPlayPlayable(nextPlayable).also {  }
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
            // Check out this variables declaration for how pitch results are handled
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

    private fun assertSessionNotStarted() {
        if (sessionJob != null) {
            throw IllegalArgumentException("startSession() called before endSession()")
        }
    }

    enum class State {
        LISTENING,
        PLAYING,
        INACTIVE,
    }

}

