package com.cannonballapps.notechaser

import app.cash.turbine.test
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.toPlayable
import com.cannonballapps.notechaser.exercisesession.SessionSettings
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2.RequiredData
import com.cannonballapps.notechaser.exercisesession.SessionViewModelDataLoader
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import runStandardCoroutineTest
import runUnconfinedCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals

class SessionViewModel2Test {

    private val playableGenerator: PlayableGenerator = mock()
    private val playablePlayer: PlayablePlayer = mock()

    private val dataLoaderFlow = Channel<ResultOf<RequiredData>>()
    private val dataLoader: SessionViewModelDataLoader = mock {
        on(it.requiredData()).doReturn(dataLoaderFlow.receiveAsFlow())
    }

    private lateinit var viewModel: SessionViewModel2

    @Test
    fun `when session view model is created - questions answered is 0`() =
        runUnconfinedCoroutineTest {
            initViewModel()

            assertEquals(
                expected = 0,
                actual = viewModel.screenUiData.value.questionsAnswered,
            )
        }

    @Test
    fun `when session view model is created - session state is Loading`() =
        runStandardCoroutineTest {
            initViewModel()

            viewModel.screenUiData.test {
                assertEquals(
                    expected = SessionState.Loading,
                    actual = awaitItem().state,
                )
            }
        }

    @Test
    fun `when playable generator fails to load - session state is Error`() = runStandardCoroutineTest {
        initViewModel()
        dataLoaderFlow.send(ResultOf.Failure(Exception()))

        viewModel.screenUiData.drop(1).test {
            assertEquals(
                expected = SessionState.Error,
                actual = awaitItem().state,
            )
        }
    }

    @Test
    fun `when playable generator loads successfully and should not play reference pitch - session state is PreStart then PlayingQuestion`() =
        runStandardCoroutineTest {
            val playable = playable()
            val settings = sessionSettings(
                sessionKey = PitchClass.C,
                shouldPlayReferencePitch = false,
            )
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)

            initViewModel()
            dataLoaderFlow.send(
                ResultOf.Success(
                    RequiredData(
                        playableGenerator = playableGenerator,
                        sessionSettings = settings,
                    )
                )
            )

            viewModel.screenUiData.drop(2).test {
                assertEquals(
                    expected = SessionState.PlayingQuestion,
                    actual = awaitItem().state,
                )
            }

            verify(playablePlayer).playPlayable(playable)
        }

    @Test
    fun `when session config loads successfully and should play starting pitch - session state is PlayingReferencePitch`() =
        runStandardCoroutineTest {
            val playable = playable()
            val settings = sessionSettings(
                sessionKey = PitchClass.C,
                shouldPlayReferencePitch = true,
            )
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)

            initViewModel()
            dataLoaderFlow.send(
                ResultOf.Success(
                    RequiredData(
                        playableGenerator = playableGenerator,
                        sessionSettings = settings,
                    )
                )
            )

            viewModel.screenUiData.drop(2).test {
                assertEquals(
                    expected = SessionState.PlayingReferencePitch(PitchClass.C),
                    actual = awaitItem().state,
                )
            }

            verify(playablePlayer).playPlayable(PitchClass.C.toPlayable())
        }

    private fun sessionSettings(
        sessionKey: PitchClass,
        shouldPlayReferencePitch: Boolean,
    ) = SessionSettings(
        sessionKey = sessionKey,
        shouldPlayReferencePitch = shouldPlayReferencePitch,
    )

    private fun initViewModel() {
        viewModel = SessionViewModel2(
            playablePlayer = playablePlayer,
            dataLoader = dataLoader,
        )
    }

    private fun playable(): Playable =
        Playable(
            notes = listOf(),
            playbackType = PlaybackType.HARMONIC,
        )
}
