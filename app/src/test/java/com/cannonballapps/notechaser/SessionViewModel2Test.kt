package com.cannonballapps.notechaser

import android.util.Range
import app.cash.turbine.test
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.NotePoolType
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.common.SessionQuestionSettings
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.common.toPlayable
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.musicutilities.Ionian
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.ParentScale
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import runStandardCoroutineTest
import runUnconfinedCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals

class SessionViewModel2Test {

    private val prefsStore: PrefsStore = mock()
    private val playableGenerator: PlayableGenerator = mock()
    private val playablePlayer: PlayablePlayer = mock()

    private lateinit var viewModel: SessionViewModel2

    @Test
    fun `when session view model is created - questions answered is 0`() =
        runUnconfinedCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

            initViewModel()

            assertEquals(
                expected = 0,
                actual = viewModel.screenUiData.value.questionsAnswered,
            )
        }

    @Test
    fun `when session view model is created - session state is Loading`() =
        runStandardCoroutineTest {
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

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
        whenever(prefsStore.playableGeneratorFlow())
            .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

        initViewModel()

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
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(flowOf(ResultOf.Success(playableGenerator)))

            val exerciseSettings = exerciseSettings(
                questionKey = PitchClass.C,
                shouldPlayReferencePitch = false,
            )
            whenever(prefsStore.exerciseSettingsFlow())
                .doReturn(flowOf(exerciseSettings))

            initViewModel()

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
            whenever(playableGenerator.generatePlayable())
                .doReturn(playable)
            whenever(prefsStore.playableGeneratorFlow())
                .doReturn(flowOf(ResultOf.Success(playableGenerator)))

            val exerciseSettings = exerciseSettings(
                questionKey = PitchClass.C,
                shouldPlayReferencePitch = true,
            )
            whenever(prefsStore.exerciseSettingsFlow())
                .doReturn(flowOf(exerciseSettings))

            initViewModel()

            viewModel.screenUiData.drop(2).test {
                assertEquals(
                    expected = SessionState.PlayingReferencePitch(PitchClass.C),
                    actual = awaitItem().state,
                )
            }

            verify(playablePlayer).playPlayable(PitchClass.C.toPlayable())
        }

    // TODO make a tighter data type for Session Settings
    private fun exerciseSettings(
        questionKey: PitchClass,
        shouldPlayReferencePitch: Boolean,
    ) = ExerciseSettings(
        notePoolType = NotePoolType.Diatonic(
            degrees = booleanArrayOf(true),
            scale = ParentScale.Major.Ionian,
        ),
        sessionQuestionSettings = SessionQuestionSettings(
            questionKey = questionKey,
            questionKeyValues = listOf(),
            shouldMatchOctave = false,
            shouldPlayStartingPitch = shouldPlayReferencePitch,
            playableBounds = Range(Note(0), Note(10)),
        ),
        sessionLengthSettings = SessionLengthSettings.NoLimit,
    )

    private fun initViewModel() {
        viewModel = SessionViewModel2(
            prefsStore = prefsStore,
            playablePlayer = playablePlayer,
        )
    }

    private fun playable(): Playable =
        Playable(
            notes = listOf(),
            playbackType = PlaybackType.HARMONIC,
        )
}
