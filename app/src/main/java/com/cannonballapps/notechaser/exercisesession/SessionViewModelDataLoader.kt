package com.cannonballapps.notechaser.exercisesession

import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.common.runCatchingToResultOfSuspending
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import kotlinx.coroutines.flow.first

class SessionViewModelDataLoader(
    private val prefsStore: PrefsStore,
) {
    suspend fun load(): ResultOf<PlayableGenerator> {
        return runCatchingToResultOfSuspending {
            (prefsStore.playableGeneratorFlow().first() as ResultOf.Success).value
        }
    }
}

