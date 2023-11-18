package com.cannonballapps.notechaser.exercisesession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGeneratorFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SessionViewModel2(
    prefsStore: PrefsStore,
    playableGeneratorFactory: PlayableGeneratorFactory,
) : ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        viewModelScope.launch {
            val settings: ExerciseSettings = prefsStore.exerciseSettingsFlow().first()
            playableGeneratorFactory.build(settings)
        }
    }
}

sealed interface SessionState {
    object Loading : SessionState
}
