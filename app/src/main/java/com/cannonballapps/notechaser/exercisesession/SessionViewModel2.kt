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
import kotlinx.coroutines.launch

class SessionViewModel2(
    prefsStore: PrefsStore,
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private lateinit var playableGenerator: PlayableGenerator

    init {
        viewModelScope.launch {
             when (val generatorResult = prefsStore.playableGeneratorFlow().first()) {
                is ResultOf.Success -> {
                    playableGenerator = generatorResult.value
                    _sessionState.value = SessionState.PreStart.also {
                        delay(it.millisUntilStart)
                    }

                    _sessionState.value = SessionState.PlayingQuestion(playableGenerator.generatePlayable())
                }
                is ResultOf.Failure -> {
                    _sessionState.value = SessionState.Error
                }
            }
        }
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
