import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class)
fun runUnconfinedCoroutineTest(testBody: suspend TestScope.() -> Unit) {
    try {
        runTest {
            Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
            testBody()
        }
    } finally {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun runStandardCoroutineTest(testBody: suspend TestScope.() -> Unit) {
    try {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            testBody()
        }
    } finally {
        Dispatchers.resetMain()
    }
}
