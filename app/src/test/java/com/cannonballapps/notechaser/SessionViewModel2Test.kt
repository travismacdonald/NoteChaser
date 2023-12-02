package com.cannonballapps.notechaser

import app.cash.turbine.test
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.toPlayable
import com.cannonballapps.notechaser.exercisesession.SessionScreenUiData
import com.cannonballapps.notechaser.exercisesession.SessionSettings
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2.RequiredData
import com.cannonballapps.notechaser.exercisesession.SessionViewModelDataLoader
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import runStandardCoroutineTest
import runUnconfinedCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun `Loading state should load required data and move to Error state on failure`() =
        runStandardCoroutineTest {
            initViewModel()
            dataLoaderFlow.send(ResultOf.Failure(Exception()))

            viewModel.screenUiData.startObservingOnState<SessionState.Loading>().test {
                assertEquals(
                    expected = SessionState.Loading,
                    actual = awaitItem().state,
                )
                verify(dataLoader).requiredData()

                assertEquals(
                    expected = SessionState.Error,
                    actual = awaitItem().state,
                )
            }
        }

    @Test
    fun `when playable generator loads successfully - session state is PreStart for 3 seconds`() =
        runStandardCoroutineTest {
            val settings = sessionSettings(
                sessionKey = PitchClass.C,
                shouldPlayReferencePitch = true,
            )

            initViewModel()
            dataLoaderFlow.send(
                ResultOf.Success(
                    RequiredData(
                        playableGenerator = playableGenerator,
                        sessionSettings = settings,
                    )
                )
            )

            viewModel.screenUiData.test {
                skipItems(1)
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = awaitItem().state,
                )
            }

            advanceTimeBy(3_000)
            assertEquals(
                expected = SessionState.PreStart,
                actual = viewModel.screenUiData.value.state,
            )

            advanceTimeBy(1)
            assertNotEquals(
                actual = viewModel.screenUiData.value.state,
                illegal = SessionState.PreStart,
            )
        }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `PlayingQuestion state should play playable and advance to Listening state`() =
        runStandardCoroutineTest {
            val playable = playable()
            val settings = sessionSettings(
                sessionKey = PitchClass.C,
                shouldPlayReferencePitch = false,
            )
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)

            whenever(playablePlayer.playPlayable2(any(), any())).then {
                launch {
                    delay(1)
                    (it.arguments[1] as () -> Unit).invoke()
                }
            }

            initViewModel()
            dataLoaderFlow.send(
                ResultOf.Success(
                    RequiredData(
                        playableGenerator = playableGenerator,
                        sessionSettings = settings,
                    )
                )
            )

            viewModel.screenUiData.startObservingOnState<SessionState.PlayingQuestion>().test {
                assertEquals(
                    expected = SessionState.PlayingQuestion,
                    actual = awaitItem().state,
                )
                verify(playablePlayer).playPlayable2(eq(playable), any())

                assertEquals(
                    expected = SessionState.Listening,
                    actual = awaitItem().state,
                )
            }
        }

    @Test
    fun `when session config loads successfully and should play starting pitch - session state is PlayingReferencePitch after PreStart`() =
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

            viewModel.screenUiData.test {
                skipItems(1)
                assertIs<SessionState.PreStart>(awaitItem().state)
                assertEquals(
                    expected = SessionState.PlayingReferencePitch(PitchClass.C),
                    actual = awaitItem().state,
                )
                assertIs<SessionState.PlayingQuestion>(awaitItem().state)
            }

            verify(playablePlayer).playPlayable2(PitchClass.C.toPlayable())
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

    private inline fun <reified T : SessionState> Flow<SessionScreenUiData>.startObservingOnState() =
        this.dropWhile { it.state !is T }
}
