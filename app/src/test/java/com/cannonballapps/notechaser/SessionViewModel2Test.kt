package com.cannonballapps.notechaser

import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesession.SessionState
import com.cannonballapps.notechaser.exercisesession.SessionViewModel2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import runCoroutineTest
import java.lang.Exception
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SessionViewModel2Test {

    private val prefsStore: PrefsStore = mock()
    private lateinit var viewModel: SessionViewModel2

    @Test
    fun `when session view model is created - session state is Loading`() =
        runCoroutineTest(StandardTestDispatcher()) {
            initViewModel()

            assertEquals(
                expected = SessionState.Loading,
                actual = viewModel.sessionState.value,
            )
        }

    @Test
    fun `when playable generator fails to load - session state is Error`() = runCoroutineTest {
        whenever(prefsStore.playableGeneratorFlow())
            .doReturn(MutableStateFlow(ResultOf.Failure(Exception())))

        initViewModel()

        assertEquals(
            expected = SessionState.Error,
            actual = viewModel.sessionState.value,
        )
    }

    private fun initViewModel() {
        viewModel = SessionViewModel2(
            prefsStore = prefsStore,
        )
    }
}
