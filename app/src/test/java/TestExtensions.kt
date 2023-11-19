import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class)
fun runCoroutineTest(
    dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    testBody: TestScope.() -> Unit,
) {
    try {
        runTest {
            Dispatchers.setMain(dispatcher)
            testBody()
        }
    } finally {
        Dispatchers.resetMain()
    }
}
