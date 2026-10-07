package com.jetbase.core.common.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppResultTest {
    @Test
    fun `emits Loading, then Success and finally Error`() = runTest {
        flow {
            emit(1)
            throw IllegalStateException("boom")
        }
            .asResult()
            .test {
                assertEquals(AppResult.Loading, awaitItem())
                assertEquals(AppResult.Success(1), awaitItem())
                val error = awaitItem()
                assertTrue(error is AppResult.Error)
                assertEquals("boom", (error as AppResult.Error).exception.message)
                awaitComplete()
            }
    }
}
