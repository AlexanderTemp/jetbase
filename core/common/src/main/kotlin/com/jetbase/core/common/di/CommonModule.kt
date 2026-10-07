package com.jetbase.core.common.di

import com.jetbase.core.common.time.SystemTimeProvider
import com.jetbase.core.common.time.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val commonModule = module {
    single<CoroutineDispatcher>(Qualifiers.IoDispatcher) { Dispatchers.IO }
    single<CoroutineDispatcher>(Qualifiers.DefaultDispatcher) { Dispatchers.Default }
    single<CoroutineScope>(Qualifiers.ApplicationScope) {
        // SupervisorJob: if a child coroutine fails, it does NOT cancel its siblings.
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(Qualifiers.DefaultDispatcher))
    }
    single<TimeProvider> { SystemTimeProvider }
}
