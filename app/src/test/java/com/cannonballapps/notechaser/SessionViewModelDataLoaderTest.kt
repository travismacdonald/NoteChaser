package com.cannonballapps.notechaser

import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesession.SessionViewModelDataLoader
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import runUnconfinedCoroutineTest
import kotlin.test.assertIs

class SessionViewModelDataLoaderTest {

    private val prefsStore: PrefsStore = mock()

    @Test
    fun `when playableGenerator fails to load - result is Failure`() =
        runUnconfinedCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(flowOf(ResultOf.Failure(Exception())))

            whenever(prefsStore.exerciseSettingsFlow())
                .doReturn(flowOf(ResultOf.Success(mock())))

            assertIs<ResultOf.Failure>(
                value = createDataLoader().requiredData().first(),
            )
        }

    @Test
    fun `when session settings fails to load - result is Failure`() =
        runUnconfinedCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(flowOf(ResultOf.Success(mock())))

            whenever(prefsStore.exerciseSettingsFlow())
                .doReturn(flowOf(ResultOf.Failure(Exception())))

            assertIs<ResultOf.Failure>(
                value = createDataLoader().requiredData().first(),
            )
        }

    @Test
    fun `when all required data loads successfully - result is Success`() =
        runUnconfinedCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(flowOf(ResultOf.Success(mock())))

            whenever(prefsStore.exerciseSettingsFlow())
                .doReturn(flowOf(ResultOf.Success(mock())))

            assertIs<ResultOf.Success<PlayableGenerator>>(
                value = createDataLoader().requiredData().first(),
            )
        }

    private fun createDataLoader() =
        SessionViewModelDataLoader(
            prefsStore = prefsStore,
        )
}
