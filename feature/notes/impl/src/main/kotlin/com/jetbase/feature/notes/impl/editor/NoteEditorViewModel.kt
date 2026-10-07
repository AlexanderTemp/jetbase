package com.jetbase.feature.notes.impl.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbase.core.data.repository.NotesRepository
import com.jetbase.core.model.Note
import com.jetbase.feature.notes.api.NoteEditorNavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Receives its navigation key as a constructor parameter (Koin passes it with parametersOf). The
 * ViewModel therefore depends on neither SavedStateHandle nor Navigation, and a test only needs
 * `NoteEditorViewModel(NoteEditorNavKey(7), fakeRepository)`. Now in Android does the same with
 * Hilt assisted injection.
 */
class NoteEditorViewModel(
    private val navKey: NoteEditorNavKey,
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var noteId: Long = navKey.noteId

    init {
        load()
    }

    private fun load() {
        if (noteId == Note.NEW_NOTE_ID) {
            _uiState.update { it.copy(isLoading = false, isNewNote = true) }
            return
        }
        viewModelScope.launch {
            val note = notesRepository.getNote(noteId)
            _uiState.update {
                if (note == null) it.copy(isLoading = false, isFinished = true)
                else
                    it.copy(
                        isLoading = false,
                        isNewNote = false,
                        title = note.title,
                        content = note.content,
                        isPinned = note.isPinned,
                    )
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value) }

    fun onContentChange(value: String) = _uiState.update { it.copy(content = value) }

    fun onTogglePin() = _uiState.update { it.copy(isPinned = !it.isPinned) }

    fun onSave() {
        val state = _uiState.value
        if (!state.canSave) return
        viewModelScope.launch {
            noteId =
                notesRepository.saveNote(
                    title = state.title,
                    content = state.content,
                    isPinned = state.isPinned,
                    id = noteId,
                )
            _uiState.update { it.copy(isFinished = true) }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            if (noteId != Note.NEW_NOTE_ID) notesRepository.deleteNote(noteId)
            _uiState.update { it.copy(isFinished = true) }
        }
    }
}
