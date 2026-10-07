package com.jetbase.feature.notes.impl.list

import app.cash.turbine.test
import com.jetbase.core.model.Note
import com.jetbase.core.testing.repository.FakeNotesRepository
import com.jetbase.core.testing.rules.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotesViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeNotesRepository()

    @Test
    fun `with no notes the state is Empty`() = runTest {
        val viewModel = NotesViewModel(repository)
        viewModel.uiState.test { assertEquals(NotesUiState.Empty, awaitItem()) }
    }

    @Test
    fun `with notes the state is Success and pinned notes come first`() = runTest {
        repository.setNotes(
            listOf(
                Note(1, "normal", "", isPinned = false, createdAt = 0, updatedAt = 0),
                Note(2, "fijada", "", isPinned = true, createdAt = 0, updatedAt = 0),
            )
        )
        val viewModel = NotesViewModel(repository)
        viewModel.uiState.test {
            val state = awaitItem() as NotesUiState.Success
            assertEquals(listOf("fijada", "normal"), state.notes.map { it.title })
        }
    }

    @Test
    fun `when the repository fails the state is Error`() = runTest {
        repository.failWith = IllegalStateException("disco lleno")
        val viewModel = NotesViewModel(repository)
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is NotesUiState.Error)
            assertEquals("disco lleno", (state as NotesUiState.Error).message)
        }
    }

    @Test
    fun `pinning a note changes its state`() = runTest {
        repository.setNotes(listOf(Note(1, "a", "", false, 0, 0)))
        val viewModel = NotesViewModel(repository)
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTogglePin(noteId = 1, isPinned = false)
            val state = awaitItem() as NotesUiState.Success
            assertTrue(state.notes.single().isPinned)
        }
    }
}
