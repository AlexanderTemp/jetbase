package com.jetbase.core.common.di

import org.koin.core.qualifier.named

/**
 * Koin qualifiers. They are the equivalent of Hilt's @IoDispatcher / @ApplicationScope annotations.
 *
 * Never write `Dispatchers.IO` directly inside a class: INJECT it, so tests can replace it with a
 * test dispatcher.
 */
object Qualifiers {
    val IoDispatcher = named("IoDispatcher")
    val DefaultDispatcher = named("DefaultDispatcher")

    /** Scope that lives as long as the app process (replaces Seal's App.applicationScope). */
    val ApplicationScope = named("ApplicationScope")
}
