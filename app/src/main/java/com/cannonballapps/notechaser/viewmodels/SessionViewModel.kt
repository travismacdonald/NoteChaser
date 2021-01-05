package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.getModeAtIx
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SessionViewModel @ViewModelInject constructor(
        private val prefsStore: PrefsStore,
) : ViewModel() {

    private val _sessionState = MutableLiveData(SessionState.INACTIVE)
    val sessionState: LiveData<SessionState>
        get() = _sessionState

    private val _curPlayable = MutableLiveData<Playable>()
    val curPlayable: LiveData<Playable>
        get() = _curPlayable

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer

    private var sessionJob: Job? = null
    private var playPlayableJob: Job? = null
    private var cancelPlayableJob: Job? = null

    fun startSession() {
        assertSessionNotStarted()

        if (this::playablePlayer.isInitialized) {
            cancelPlayableJob?.cancel()
            sessionJob = launchSession()
        }
    }

    fun endSession() {
        if (sessionJob == null) {
            return
        }
        sessionJob?.cancel()
        sessionJob = null

        playPlayableJob?.cancel()

        cancelPlayableJob = launchCancelPlayable()
    }

    fun initGenerator(type: ExerciseType) {
        viewModelScope.launch {
            makePlayableGenerator(type)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playablePlayer.midiPlayer.stop()
    }

    private fun launchSession(): Job {
        return viewModelScope.launch {

            val nextPlayable = generator.generatePlayable()
            _curPlayable.value = nextPlayable

            playPlayableJob = launchPlayableJob(nextPlayable)
            playPlayableJob?.join()

        }
    }

    private fun launchPlayableJob(playable: Playable): Job {
        return viewModelScope.launch {
            _sessionState.value = SessionState.PLAYING
            playablePlayer.playPlayable(playable)
            _sessionState.value = SessionState.INACTIVE
        }
    }

    private fun launchCancelPlayable(): Job {
        return viewModelScope.launch {
            playablePlayer.stopCurPlayable()
            _sessionState.value = SessionState.INACTIVE
        }
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

    enum class SessionState {
        LISTENING,
        PLAYING,
        INACTIVE,
    }


}

