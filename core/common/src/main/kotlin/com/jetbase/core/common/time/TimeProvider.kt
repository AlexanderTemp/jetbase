package com.jetbase.core.common.time

/**
 * Clock abstraction. Calling System.currentTimeMillis() directly makes code impossible to test
 * deterministically; by injecting a TimeProvider the test controls the time.
 */
fun interface TimeProvider {
    fun now(): Long
}

object SystemTimeProvider : TimeProvider {
    override fun now(): Long = System.currentTimeMillis()
}
