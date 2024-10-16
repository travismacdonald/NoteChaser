package com.cannonballapps.notechaser.exercisesession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.AnswerTracker
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.noteprocessor.NoteDetectionResult
import com.cannonballapps.notechaser.common.noteprocessor.NoteDetector
import com.cannonballapps.notechaser.common.toPlayable
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel2(
    private val playablePlayer: PlayablePlayer,
    private val dataLoader: SessionViewModelDataLoader,
    private val answerTracker: AnswerTracker,
    noteDetector: NoteDetector,
) : ViewModel() {

    companion object {
        private const val NO_INPUT_THRESHOLD_MILLIS = 3_000L
        private const val DETECTED_NOTE_PROBABILITY_THRESHOLD = 0.8f
    }

    data class RequiredData(
        val playableGenerator: PlayableGenerator,
        val sessionSettings: SessionSettings,
    )

    private interface SessionStateHandler {
        fun enterState()
    }

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    private val _numberOfCorrectAnswers: MutableStateFlow<Int> = MutableStateFlow(0)
    private val noteDetectionFlow = noteDetector.noteDetectionFlow.distinctUntilChanged()

    private lateinit var requiredData: RequiredData

    private val playableGenerator: PlayableGenerator
        get() = requiredData.playableGenerator
    private val sessionKey: PitchClass
        get() = requiredData.sessionSettings.sessionKey
    private val shouldPlayReferencePitch: Boolean
        get() = requiredData.sessionSettings.shouldPlayReferencePitch
    private val numberOfQuestions: Int
        get() = requiredData.sessionSettings.totalQuestions

    private lateinit var currentPlayable: Playable

    val screenUiData: StateFlow<SessionScreenUiData> = combine(
        _sessionState,
        _numberOfCorrectAnswers,
    ) {
        sessionState,
        questionsAnswered ->
        SessionScreenUiData(
            state = sessionState,
            numberOfCorrectAnswers = questionsAnswered,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SessionScreenUiData(
            state = _sessionState.value,
            numberOfCorrectAnswers = _numberOfCorrectAnswers.value,
        )
    )

    private val loadingStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.Loading

            viewModelScope.launch {
                when (val requiredDataResult = dataLoader.requiredData().first()) {
                    is ResultOf.Success -> {
                        requiredData = requiredDataResult.value
                        sessionInitializationHandler.enterState()
                    }
                    is ResultOf.Failure -> {
                        errorStateHandler.enterState()
                    }
                }
            }
        }
    }

    private val errorStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.Error
        }
    }

    private val sessionInitializationHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.SessionInitialization
            updateCurrentPlayable()

            viewModelScope.launch {
                delay(SessionState.SessionInitialization.millisUntilStart)
                if (shouldPlayReferencePitch) {
                    playingReferencePitchHandler.enterState()
                } else {
                    playingQuestionHandler.enterState()
                }
            }
        }
    }

    private val playingReferencePitchHandler = object : SessionStateHandler {
        override fun enterState() {
            val state = SessionState.PlayingReferencePitch(sessionKey)
            _sessionState.value = state

            viewModelScope.launch {
                playablePlayer.playPlayable2(sessionKey.toPlayable())
                delay(state.referencePitchDurationMillis)
                playingQuestionHandler.enterState()
            }
        }
    }

    private val playingQuestionHandler: SessionStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.PlayingQuestion

            playablePlayer.playPlayable2(
                playable = currentPlayable,
                onPlaybackFinished = {
                    listeningHandler.enterState()
                },
            )
        }
    }

    private val listeningHandler = object : SessionStateHandler {
        private var replayQuestionJob: Job? = null
        private var noteDetectionJob: Job? = null

        override fun enterState() {
            noteDetectionJob = noteDetectionFlow
                .onEach(::onNoteDetectionResult)
                .launchIn(viewModelScope)
        }

        private fun onNoteDetectionResult(result: NoteDetectionResult) {
            updateState(result)
            clearScheduledReplayQuestionJobIfAny()

            when (result) {
                is NoteDetectionResult.Value -> onValueResult(result)
                NoteDetectionResult.None -> onNoneResult()
            }
        }

        private fun onValueResult(result: NoteDetectionResult.Value) {
            if (result.meetsThreshold() && result.isDifferentFromLastTrackedNote()) {
                if (answerTracker.trackNote(result.note)) {
                    stopNoteDetection()
                    answerCorrectHandler.enterState()
                }
            }
        }

        private fun onNoneResult() {
            scheduleReplayQuestionJob()
        }

        private fun updateState(result: NoteDetectionResult) {
            _sessionState.value = SessionState.Listening(result)
        }

        private fun clearScheduledReplayQuestionJobIfAny() {
            replayQuestionJob?.cancel()
            replayQuestionJob = null
        }

        private fun scheduleReplayQuestionJob() {
            replayQuestionJob = viewModelScope.launch {
                delay(NO_INPUT_THRESHOLD_MILLIS)
                stopNoteDetection()
                playingQuestionHandler.enterState()
            }
        }

        private fun stopNoteDetection() {
            noteDetectionJob?.cancel()
            noteDetectionJob = null
        }

        private fun NoteDetectionResult.Value.isDifferentFromLastTrackedNote() =
            answerTracker.lastTrackedNote != this.note

        private fun NoteDetectionResult.Value.meetsThreshold() =
            this.probability >= DETECTED_NOTE_PROBABILITY_THRESHOLD
    }

    private val answerCorrectHandler: SessionStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.AnswerCorrect
            this@SessionViewModel2._numberOfCorrectAnswers.value++

            viewModelScope.launch {
                delay(2_000L)
                if (this@SessionViewModel2._numberOfCorrectAnswers.value == numberOfQuestions) {
                    _sessionState.value = SessionState.SessionComplete
                } else {
                    updateCurrentPlayable()
                    playingQuestionHandler.enterState()
                }
            }
        }
    }

    init {
        loadingStateHandler.enterState()
    }

    private fun updateCurrentPlayable() {
        currentPlayable = playableGenerator.generatePlayable().also { playable ->
            answerTracker.setExpectedAnswer(playable)
        }
    }
}

data class SessionScreenUiData(
    val state: SessionState,
    val numberOfCorrectAnswers: Int,
)

sealed interface SessionState {

    object Loading : SessionState

    object Error : SessionState

    object SessionInitialization : SessionState {
        const val millisUntilStart = 3_000L
    }

    data class PlayingReferencePitch(
        val referencePitch: PitchClass,
    ) : SessionState {
        val referencePitchDurationMillis = 2_000L
    }

    object PlayingQuestion : SessionState

    data class Listening(val result: NoteDetectionResult) : SessionState

    object AnswerCorrect : SessionState

    object SessionComplete : SessionState
}

data class SessionSettings(
    val shouldPlayReferencePitch: Boolean,
    val sessionKey: PitchClass,
    val totalQuestions: Int,
)
