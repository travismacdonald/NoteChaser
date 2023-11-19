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

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private lateinit var playableGenerator: PlayableGenerator

    init {
        startStateChangeListener()
        loadPlayableGenerator()
    }

    private fun startStateChangeListener() {
        sessionState
            .onEach(::onStateChanged)
            .launchIn(viewModelScope)
    }

    private fun loadPlayableGenerator() {
        viewModelScope.launch {
            when (val generatorResult = prefsStore.playableGeneratorFlow().first()) {
                is ResultOf.Success -> {
                    onPlayableGeneratorLoadSuccess(generatorResult.value)
                }
                is ResultOf.Failure -> {
                    onPlayableGeneratorLoadFailure()
                }
            }
        }
    }

    private suspend fun onStateChanged(it: SessionState) {
        when (it) {
            is SessionState.PreStart -> {
                delay(it.millisUntilStart)
                enterPlayingQuestionState()
            }

            is SessionState.Error,
            is SessionState.Loading,
            is SessionState.PlayingQuestion,
            -> { /* No-op */ }
        }
    }

    private fun onPlayableGeneratorLoadSuccess(generator: PlayableGenerator) {
        this.playableGenerator = generator
        _sessionState.value = SessionState.PreStart
    }

    private fun onPlayableGeneratorLoadFailure() {
        _sessionState.value = SessionState.Error
    }

    private fun enterPlayingQuestionState() {
        _sessionState.value = SessionState.PlayingQuestion(playableGenerator.generatePlayable())
    }
}

sealed interface SessionState {
    object Loading : SessionState
    object Error : SessionState
    object PreStart : SessionState {
        const val millisUntilStart = 3_000L
    }
    data class PlayingQuestion(val playable: Playable) : SessionState
}
