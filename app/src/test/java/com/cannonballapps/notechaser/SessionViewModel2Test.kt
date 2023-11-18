package com.cannonballapps.notechaser

import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

class SessionViewModel2Test {

    @Test
    fun `when session view model is created - session state is Loading`() {
        val viewModel = SessionViewModel2()
        assertEquals(
            expected = SessionState.Loading,
            actual = viewModel.state.value,
        )
    }

    @Test
    fun `when session data has failed to load - session state is Error`() {
        val viewModel = SessionViewModel2(
            prefsStore = PrefsStore(
                context = mock()
            )
        )

        // todo emit bad data from prefs store

        assertEquals(
            expected = SessionState.Error,
            actual = viewModel.state.value,
        )
    }
}
