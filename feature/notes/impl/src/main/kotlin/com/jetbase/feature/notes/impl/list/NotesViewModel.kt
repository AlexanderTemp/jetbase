package com.jetbase.feature.notes.impl.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbase.core.common.result.AppResult
import com.jetbase.core.common.result.asResult
import com.jetbase.core.data.repository.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The ViewModel receives its dependencies through the CONSTRUCTOR (Koin provides them). Compare it
 * with Seal's ViewModels, which call `DatabaseUtil` (a global object) from the inside.
 */
class NotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    val uiState: StateFlow<NotesUiState> =
        notesRepository
            .observeNotes()
            .asResult()
            .map { result ->
                when (result) {
                    AppResult.Loading -> NotesUiState.Loading
                    is AppResult.Error -> NotesUiState.Error(result.exception.message)
                    is AppResult.Success ->
                        if (result.data.isEmpty()) NotesUiState.Empty
                        else NotesUiState.Success(result.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                // Keeps the subscription 5 s after the UI leaves: survives a rotation without
                // querying the database again.
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NotesUiState.Loading,
            )

    fun onTogglePin(noteId: Long, isPinned: Boolean) {
        viewModelScope.launch { notesRepository.setPinned(noteId, !isPinned) }
    }
}
