package com.cannonballapps.notechaser.exercisesession

import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.common.runCatchingToResultOfSuspending
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SessionViewModelDataLoader(
    private val prefsStore: PrefsStore,
) {
    fun requiredData(): Flow<ResultOf<SessionViewModel2.RequiredData>> = combine(
        prefsStore.playableGeneratorFlow(),
        prefsStore.exerciseSettingsFlow(),
    ) { playableGenerator, exerciseSettings ->
        runCatchingToResultOfSuspending {
            SessionViewModel2.RequiredData(
                playableGenerator = (playableGenerator as ResultOf.Success).value,
                sessionSettings = (exerciseSettings as ResultOf.Success).value,
            )
        }
    }
}
