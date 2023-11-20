package com.cannonballapps.notechaser.exercisesession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SessionViewModel2(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.None)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private lateinit var playableGenerator: PlayableGenerator

    interface SessionStateHandler<T> {
        fun createState(): T
        fun enterState(state: T) { /* Default no-op */ }
    }

    private val loadingStateHandler = object : SessionStateHandler<SessionState.Loading> {
        override fun createState() = SessionState.Loading

        override fun enterState(state: SessionState.Loading) {
            viewModelScope.launch {
                when (val generatorResult = prefsStore.playableGeneratorFlow().first()) {
                    is ResultOf.Success -> {
                        playableGenerator = generatorResult.value
                        changeStateTo(preStartStateHandler.createState())
                    }
                    is ResultOf.Failure -> {
                        changeStateTo(errorStateHandler.createState())
                    }
                }
            }
        }
    }

    private val errorStateHandler = object : SessionStateHandler<SessionState.Error> {
        override fun createState() = SessionState.Error
    }

    private val preStartStateHandler = object : SessionStateHandler<SessionState.PreStart> {
        override fun createState() = SessionState.PreStart

        override fun enterState(state: SessionState.PreStart) {
            viewModelScope.launch {
                delay(state.millisUntilStart)
                changeStateTo(playingQuestionHandler.createState())
            }
        }
    }

    private val playingQuestionHandler = object : SessionStateHandler<SessionState.PlayingQuestion> {
        override fun createState() =
            SessionState.PlayingQuestion(playableGenerator.generatePlayable())
    }

    init {
        startStateChangeListener()
        changeStateTo(loadingStateHandler.createState())
    }

    private fun changeStateTo(sessionState: SessionState) {
        _sessionState.value = sessionState
    }

    private fun startStateChangeListener() {
        sessionState
            .onEach(::onStateChanged)
            .launchIn(viewModelScope)
    }

    private fun onStateChanged(state: SessionState) {
        when (state) {
            is SessionState.Loading -> loadingStateHandler.enterState(state)
            is SessionState.Error -> errorStateHandler.enterState(state)
            is SessionState.PreStart -> preStartStateHandler.enterState(state)
            is SessionState.PlayingQuestion -> playingQuestionHandler.enterState(state)
            is SessionState.None -> { /* No-op */ }
        }
    }
}

sealed interface SessionState {

    object None : SessionState

    object Loading : SessionState

    object Error : SessionState

    object PreStart : SessionState {
        const val millisUntilStart = 3_000L
    }

    data class PlayingQuestion(val playable: Playable) : SessionState
}
