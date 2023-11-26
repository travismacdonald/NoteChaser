package com.cannonballapps.notechaser.exercisesession

import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.common.runCatchingToResultOfSuspending
import kotlinx.coroutines.flow.first

class SessionViewModelDataLoader(
    private val prefsStore: PrefsStore,
) {
    suspend fun load(): ResultOf<SessionViewModel2.RequiredData> {
        return runCatchingToResultOfSuspending {
            SessionViewModel2.RequiredData(
                playableGenerator = (prefsStore.playableGeneratorFlow().first() as ResultOf.Success).value,
                sessionSettings = (prefsStore.exerciseSettingsFlow().first()),
            )
        }
    }
}

