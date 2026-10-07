package com.jetbase.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/** Wraps the values of a Flow in three explicit states: loading, success or error. */
sealed interface AppResult<out T> {
    data object Loading : AppResult<Nothing>

    data class Success<T>(val data: T) : AppResult<T>

    data class Error(val exception: Throwable) : AppResult<Nothing>
}

/**
 * Turns any `Flow<T>` into `Flow<AppResult<T>>`: emits Loading when it starts, Success for every
 * value and Error if the flow throws (instead of crashing the app).
 */
fun <T> Flow<T>.asResult(): Flow<AppResult<T>> =
    map<T, AppResult<T>> { AppResult.Success(it) }
        .onStart { emit(AppResult.Loading) }
        .catch { emit(AppResult.Error(it)) }
