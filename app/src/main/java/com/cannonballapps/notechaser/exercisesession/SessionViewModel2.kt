package com.cannonballapps.notechaser.exercisesession

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionViewModel2 {
    val state: StateFlow<SessionState> = MutableStateFlow(SessionState.Loading)
}

sealed interface SessionState {
    object Loading : SessionState
}
