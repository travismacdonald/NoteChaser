package com.cannonballapps.notechaser

import app.cash.turbine.test
import com.cannonballapps.notechaser.common.AnswerTracker
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.noteprocessor.NoteDetectionResult
import com.cannonballapps.notechaser.common.noteprocessor.NoteDetector
import com.cannonballapps.notechaser.common.toPlayable
import com.cannonballapps.notechaser.exercisesession.SessionScreenUiData
import com.cannonballapps.notechaser.exercisesession.SessionSettings
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2.RequiredData
import com.cannonballapps.notechaser.exercisesession.SessionViewModelDataLoader
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import runStandardCoroutineTest
import runUnconfinedCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertIs

/*
 * TODO write test that verifies we set the expected answer on each question
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SessionViewModel2Test {

    private val playableGenerator: PlayableGenerator = mock()
    private val playablePlayer: PlayablePlayer = mock()

    private val noteDetectionFlow = MutableSharedFlow<NoteDetectionResult>(replay = 1)
    private val noteDetector: NoteDetector = mock {
        on(it.noteDetectionFlow).doReturn(noteDetectionFlow)
    }

    private val answerTracker: AnswerTracker = mock()

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
                actual = viewModel.screenUiData.value.numberOfCorrectAnswers,
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
    fun `Loading state should move to Error state on failure`() =
        runStandardCoroutineTest {
            initViewModel()
            setupInitialDataFailure()

            viewModel.screenUiData.startObservingOnState<SessionState.Loading>().test {
                assertEquals(
                    expected = SessionState.Loading,
                    actual = awaitItem().state,
                )
                verify(dataLoader).requiredData()

                assertIs<SessionState.Error>(awaitItem().state)
            }
        }

    @Test
    fun `Loading state should move to PreStart state on success`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            initViewModel()
            setupInitialDataSuccess()

            viewModel.screenUiData.startObservingOnState<SessionState.Loading>().test {
                assertEquals(
                    expected = SessionState.Loading,
                    actual = awaitItem().state,
                )
                verify(dataLoader).requiredData()

                assertIs<SessionState.PreStart>(awaitItem().state)
            }
        }

    @Test
    fun `PreStart state should move to PlayingReferencePitch after delay when reference pitch is required`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            initViewModel()
            setupInitialDataSuccess(shouldPlayReferencePitch = true)

            viewModel.screenUiData.startObservingOnState<SessionState.PreStart>().test {
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = awaitItem().state,
                )

                advanceTimeBy(3_000)
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = viewModel.screenUiData.value.state,
                )

                assertIs<SessionState.PlayingReferencePitch>(awaitItem().state)
            }
        }

    @Test
    fun `PreStart state should move to PlayingQuestion after delay when reference pitch is not required`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            initViewModel()
            setupInitialDataSuccess(shouldPlayReferencePitch = false)

            viewModel.screenUiData.startObservingOnState<SessionState.PreStart>().test {
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = awaitItem().state,
                )

                advanceTimeBy(3_000)
                assertEquals(
                    expected = SessionState.PreStart,
                    actual = viewModel.screenUiData.value.state,
                )

                assertIs<SessionState.PlayingQuestion>(awaitItem().state)
            }
        }

    @Test
    fun `PlayingReferencePitch state should play reference pitch and move to PlayingQuestion`() =
        runStandardCoroutineTest {
            val referencePitch = PitchClass.C
            setupPlayableGenerator()

            initViewModel()
            setupInitialDataSuccess(
                sessionKey = referencePitch,
                shouldPlayReferencePitch = true,
            )

            viewModel.screenUiData.startObservingOnState<SessionState.PlayingReferencePitch>().test {
                assertEquals(
                    expected = SessionState.PlayingReferencePitch(
                        referencePitch = referencePitch,
                    ),
                    actual = awaitItem().state,
                )
                verify(playablePlayer).playPlayable2(referencePitch.toPlayable())

                advanceTimeBy(2_000L)
                assertEquals(
                    expected = SessionState.PlayingReferencePitch(
                        referencePitch = referencePitch,
                    ),
                    actual = viewModel.screenUiData.value.state,
                )

                assertIs<SessionState.PlayingQuestion>(awaitItem().state)
            }
        }

    @Test
    fun `PlayingQuestion state should play playable and advance to Listening state`() =
        runStandardCoroutineTest {
            val playable = playable()
            setupPlayableGenerator(playable)

            setupPlayablePlayer()

            initViewModel()
            setupInitialDataSuccess()

            viewModel.screenUiData.startObservingOnState<SessionState.PlayingQuestion>().test {
                assertEquals(
                    expected = SessionState.PlayingQuestion,
                    actual = awaitItem().state,
                )
                verify(playablePlayer).playPlayable2(eq(playable), any())

                noteDetectionFlow.tryEmit(NoteDetectionResult.None)
                assertIs<SessionState.Listening>(awaitItem().state)
            }
        }

    @Test
    fun `Listening state should listen for input`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()

            initViewModel()
            setupInitialDataSuccess()
            noteDetectionFlow.tryEmit(
                NoteDetectionResult.None,
            )

            viewModel.screenUiData.startObservingOnState<SessionState.Listening>().test {
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = awaitItem().state,
                )

                val detectionResult = NoteDetectionResult.Value(
                    note = Note(60),
                    probability = 0.5f,
                )
                noteDetectionFlow.tryEmit(detectionResult)
                assertEquals(
                    expected = SessionState.Listening(detectionResult),
                    awaitItem().state,
                )

                val detectionResult2 = NoteDetectionResult.Value(
                    note = Note(64),
                    probability = 0.3f,
                )
                noteDetectionFlow.tryEmit(detectionResult2)
                assertEquals(
                    expected = SessionState.Listening(detectionResult2),
                    awaitItem().state,
                )
            }
        }

    @Test
    fun `Listening state should advance to ReplayQuestion state after 3 seconds of no input detection`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()

            initViewModel()
            setupInitialDataSuccess()

            noteDetectionFlow.tryEmit(
                NoteDetectionResult.None,
            )
            viewModel.screenUiData.startObservingOnState<SessionState.Listening>().test {
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = awaitItem().state,
                )

                advanceTimeBy(3_000L)
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = viewModel.screenUiData.value.state,
                )

                advanceTimeBy(1L)
                assertIs<SessionState.ReplayQuestion>(viewModel.screenUiData.value.state)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Listening state should advance to ReplayQuestion state after 3 seconds of no input detection 2`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()

            initViewModel()
            setupInitialDataSuccess()

            noteDetectionFlow.tryEmit(
                NoteDetectionResult.None,
            )
            viewModel.screenUiData.startObservingOnState<SessionState.Listening>().test {
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = awaitItem().state,
                )

                advanceTimeBy(500L)
                // Detected note should reset no input timer
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        note = Note(64),
                        probability = 0.5f,
                    )
                )
                advanceTimeBy(500L)
                // Timer should start again after None result
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.None,
                )

                advanceTimeBy(3_000L)
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = viewModel.screenUiData.value.state,
                )

                advanceTimeBy(1L)
                assertIs<SessionState.ReplayQuestion>(viewModel.screenUiData.value.state)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Listening state should use AnswerTracker for note detection results`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()
            setupAnswerTracker()

            initViewModel()
            setupInitialDataSuccess()

            noteDetectionFlow.tryEmit(
                NoteDetectionResult.None,
            )

            viewModel.screenUiData.startObservingOnState<SessionState.Listening>().test {
                // Shouldn't track note since detection result is None
                verify(answerTracker, times(0)).trackNote(any())

                // Shouldn't track result since detection probability doesn't meet threshold
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        Note(60),
                        probability = 0.79f,
                    )
                )
                advanceUntilIdle()
                verify(answerTracker, times(0)).trackNote(Note(60))

                // Should track result since probability meets threshold
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        Note(60),
                        probability = 0.80f,
                    )
                )
                advanceUntilIdle()
                verify(answerTracker, times(1)).trackNote(Note(60))

                // Shouldn't track result since it's the same note as the previous one
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        Note(60),
                        probability = 0.85f,
                    )
                )
                advanceUntilIdle()
                verify(answerTracker, times(1)).trackNote(Note(60))

                // Should track result since it's a new note and probability meets threshold
                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        Note(65),
                        probability = 0.85f,
                    )
                )
                advanceUntilIdle()
                verify(answerTracker).trackNote(Note(65))

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Listening state should advance to AnswerCorrect after a correct answer`() =
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()
            setupAnswerTracker(isAnswerCorrect = true)

            initViewModel()
            setupInitialDataSuccess()

            noteDetectionFlow.tryEmit(
                NoteDetectionResult.None,
            )

            viewModel.screenUiData.startObservingOnState<SessionState.Listening>().test {
                assertEquals(
                    expected = SessionState.Listening(NoteDetectionResult.None),
                    actual = awaitItem().state,
                )

                noteDetectionFlow.tryEmit(
                    NoteDetectionResult.Value(
                        note = Note(60),
                        probability = 0.8f,
                    ),
                )
                assertIs<SessionState.AnswerCorrect>(awaitItem().state)
            }
        }

    @Test
    fun `AnswerCorrect state should increase correct answers by 1, generate new playable, and move to Playing state`() {
        runStandardCoroutineTest {
            setupPlayableGenerator()
            setupPlayablePlayer()
            setupAnswerTracker(isAnswerCorrect = true)

            initViewModel()
            setupInitialDataSuccess()

            noteDetectionFlow.tryEmit(
                NoteDetectionResult.Value(
                    note = Note(60),
                    probability = 0.8f,
                ),
            )

            viewModel.screenUiData.startObservingOnState<SessionState.AnswerCorrect>().test {
                with(awaitItem()) {
                    assertEquals(
                        expected = SessionState.AnswerCorrect,
                        actual = state,
                    )
                    assertEquals(
                        expected = 1,
                        actual = numberOfCorrectAnswers,
                    )
                }

                advanceTimeBy(2_001)
                verify(playableGenerator, times(2)).generatePlayable()

                assertIs<SessionState.PlayingQuestion>(awaitItem().state)
            }
        }
    }

    private fun setupAnswerTracker(
        isAnswerCorrect: Boolean = false,
    ) {
        var lastTrackedNote: Note? = null

        whenever(answerTracker.trackNote(any()))
            .then {
                lastTrackedNote = it.arguments[0] as Note
                isAnswerCorrect
            }

        whenever(answerTracker.lastTrackedNote)
            .doAnswer { lastTrackedNote }
    }

    private fun setupPlayableGenerator(
        playable: Playable = playable(),
    ) {
        whenever(playableGenerator.generatePlayable())
            .doReturn(playable)
    }

    private suspend fun setupInitialDataFailure() {
        dataLoaderFlow.send(ResultOf.Failure(Exception()))
    }

    private suspend fun setupInitialDataSuccess(
        sessionKey: PitchClass = PitchClass.C,
        shouldPlayReferencePitch: Boolean = false,
    ) {
        val settings = SessionSettings(
            sessionKey = sessionKey,
            shouldPlayReferencePitch = shouldPlayReferencePitch,
        )

        dataLoaderFlow.send(
            ResultOf.Success(
                RequiredData(
                    playableGenerator = playableGenerator,
                    sessionSettings = settings,
                )
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun TestScope.setupPlayablePlayer() {
        whenever(playablePlayer.playPlayable2(any(), any())).then {
            launch {
                delay(1)
                (it.arguments[1] as () -> Unit).invoke()
            }
        }
    }

    private fun initViewModel() {
        viewModel = SessionViewModel2(
            playablePlayer = playablePlayer,
            dataLoader = dataLoader,
            noteDetector = noteDetector,
            answerTracker = answerTracker,
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
