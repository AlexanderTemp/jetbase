package com.jetbase.feature.notes.impl.editor

import com.jetbase.core.model.Note
import com.jetbase.core.testing.repository.FakeNotesRepository
import com.jetbase.core.testing.rules.MainDispatcherRule
import com.jetbase.feature.notes.api.NoteEditorNavKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NoteEditorViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeNotesRepository()

    @Test
    fun `a new note starts empty and cannot be saved`() {
        val viewModel = NoteEditorViewModel(NoteEditorNavKey(), repository)
        val state = viewModel.uiState.value
        assertTrue(state.isNewNote)
        assertFalse(state.isLoading)
        assertFalse(state.canSave)
    }

    @Test
    fun `saving a new note creates it and finishes`() = runTest {
        val viewModel = NoteEditorViewModel(NoteEditorNavKey(), repository)
        viewModel.onTitleChange("Hola")
        viewModel.onSave()

        assertTrue(viewModel.uiState.value.isFinished)
        assertEquals("Hola", repository.observeNotes().first().single().title)
    }

    @Test
    fun `editing loads the existing note`() {
        repository.setNotes(listOf(Note(7, "Titulo", "Cuerpo", true, 0, 0)))
        val viewModel = NoteEditorViewModel(NoteEditorNavKey(noteId = 7), repository)

        val state = viewModel.uiState.value
        assertFalse(state.isNewNote)
        assertEquals("Titulo", state.title)
        assertTrue(state.isPinned)
    }

    @Test
    fun `deleting removes the note`() = runTest {
        repository.setNotes(listOf(Note(7, "x", "", false, 0, 0)))
        val viewModel = NoteEditorViewModel(NoteEditorNavKey(noteId = 7), repository)
        viewModel.onDelete()

        assertTrue(viewModel.uiState.value.isFinished)
        assertNull(repository.getNote(7))
    }

    @Test
    fun `a note that does not exist closes the editor`() {
        val viewModel = NoteEditorViewModel(NoteEditorNavKey(noteId = 99), repository)
        assertTrue(viewModel.uiState.value.isFinished)
    }
}
