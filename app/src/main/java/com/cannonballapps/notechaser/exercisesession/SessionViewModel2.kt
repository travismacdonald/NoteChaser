package com.cannonballapps.notechaser.exercisesession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel2(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    private interface SessionStateHandler {
        fun enterState()
    }

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    private val _questionsAnswered: MutableStateFlow<Int> = MutableStateFlow(0)

    private lateinit var playableGenerator: PlayableGenerator

    val screenUiData: StateFlow<SessionScreenUiData> = combine(
        _sessionState,
        _questionsAnswered,
    ) {
        sessionState,
        questionsAnswered ->
        SessionScreenUiData(
            state = sessionState,
            questionsAnswered = questionsAnswered,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SessionScreenUiData(
            state = _sessionState.value,
            questionsAnswered = _questionsAnswered.value,
        )
    )

    private val loadingStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.Loading

            viewModelScope.launch {
                when (val generatorResult = prefsStore.playableGeneratorFlow().first()) {
                    is ResultOf.Success -> {
                        playableGenerator = generatorResult.value
                        preStartStateHandler.enterState()
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

    private val preStartStateHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.PreStart

            viewModelScope.launch {
                delay(SessionState.PreStart.millisUntilStart)
                playingQuestionHandler.enterState()
            }
        }
    }

    private val playingQuestionHandler = object : SessionStateHandler {
        override fun enterState() {
            _sessionState.value = SessionState.PlayingQuestion(playableGenerator.generatePlayable())
        }

    }

    init {
        loadingStateHandler.enterState()
    }
}

data class SessionScreenUiData(
    val state: SessionState,
    val questionsAnswered: Int,
)

sealed interface SessionState {

    object Loading : SessionState

    object Error : SessionState

    object PreStart : SessionState {
        const val millisUntilStart = 3_000L
    }

    data class PlayingQuestion(val playable: Playable) : SessionState
}
