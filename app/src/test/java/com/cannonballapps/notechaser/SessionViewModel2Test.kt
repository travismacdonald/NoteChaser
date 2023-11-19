package com.cannonballapps.notechaser

import app.cash.turbine.test
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import runStandardCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals

class SessionViewModel2Test {

    private val prefsStore: PrefsStore = mock()
    private val playableGenerator: PlayableGenerator = mock()

    private lateinit var viewModel: SessionViewModel2

    @Test
    fun `when session view model is created - session state is Loading`() =
        runStandardCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

            initViewModel()

            viewModel.sessionState.test {
                assertEquals(
                    expected = SessionState.Loading,
                    actual = awaitItem(),
                )
            }
        }

    @Test
    fun `when playable generator fails to load - session state is Error`() = runStandardCoroutineTest {
        whenever(prefsStore.playableGeneratorFlow())
            .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

        initViewModel()

        viewModel.sessionState.drop(1).test {
            assertEquals(
                expected = SessionState.Error,
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `when playable generator loads successfully - session state is PreStart then PlayingQuestion`()
        = runStandardCoroutineTest {
            val playable = playable()
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(MutableStateFlow(ResultOf.Success(playableGenerator)))

            initViewModel()

            viewModel.sessionState.drop(1).test {
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = awaitItem(),
                )

                assertEquals(
                    expected = SessionState.PlayingQuestion(playable),
                    actual = awaitItem(),
                )
            }
        }

    private fun initViewModel() {
        viewModel = SessionViewModel2(
            prefsStore = prefsStore,
        )
    }

    private fun playable(): Playable =
        Playable(
            notes = listOf(),
            playbackType = PlaybackType.HARMONIC,
        )
}
