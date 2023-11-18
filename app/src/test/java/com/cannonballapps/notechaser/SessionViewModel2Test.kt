package com.cannonballapps.notechaser

import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGeneratorFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class SessionViewModel2Test {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    @Test
    fun `when session view model is created - session state is Loading`() {
        val viewModel = SessionViewModel2(
            prefsStore = mock(),
            playableGeneratorFactory = mock(),
        )
        assertEquals(
            expected = SessionState.Loading,
            actual = viewModel.sessionState.value,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when exercise settings data loads successfully - playable generator is built`() = runTest {
        val prefsStore: PrefsStore = mock()
        val exerciseSettings: ExerciseSettings = mock()
        whenever(prefsStore.exerciseSettingsFlow()).doReturn(MutableStateFlow(exerciseSettings))

        val playableGeneratorFactory: PlayableGeneratorFactory = mock()

        SessionViewModel2(
            prefsStore = prefsStore,
            playableGeneratorFactory = playableGeneratorFactory,
        )

        verify(playableGeneratorFactory).build(exerciseSettings)
    }
}
