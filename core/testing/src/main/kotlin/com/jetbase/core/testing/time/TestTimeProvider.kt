package com.jetbase.core.testing.time

import com.jetbase.core.common.time.TimeProvider

/** Clock controlled by the test. */
class TestTimeProvider(start: Long = 0L) : TimeProvider {
    private var current = start

    override fun now(): Long = current

    fun advanceBy(millis: Long) {
        current += millis
    }
}
