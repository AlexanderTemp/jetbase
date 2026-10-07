package com.jetbase.feature.notes.impl.list

import com.jetbase.core.model.Note

/**
 * MUTUALLY EXCLUSIVE states of the screen. With a sealed interface the compiler forces the UI to
 * handle all four cases in a `when`: the error case cannot be forgotten.
 */
sealed interface NotesUiState {
    data object Loading : NotesUiState

    data object Empty : NotesUiState

    data class Success(val notes: List<Note>) : NotesUiState

    data class Error(val message: String?) : NotesUiState
}
