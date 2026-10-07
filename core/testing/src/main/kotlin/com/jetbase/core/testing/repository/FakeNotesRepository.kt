package com.jetbase.core.testing.repository

import com.jetbase.core.data.repository.NotesRepository
import com.jetbase.core.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Fake (not a mock): a REAL but in-memory implementation of the contract. It behaves like the real
 * repository, so tests verify behaviour, not calls.
 */
class FakeNotesRepository : NotesRepository {
    private val notes = MutableStateFlow<List<Note>>(emptyList())
    private var nextId = 1L

    /** When set, every operation throws this exception (to test error paths). */
    var failWith: Throwable? = null

    fun setNotes(list: List<Note>) {
        notes.value = list
        nextId = (list.maxOfOrNull { it.id } ?: 0L) + 1
    }

    override fun observeNotes(): Flow<List<Note>> = notes.map { list ->
        failWith?.let { throw it }
        list.sortedWith(compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt })
    }

    override fun observeNote(id: Long): Flow<Note?> = notes.map { list ->
        list.find { it.id == id }
    }

    override suspend fun getNote(id: Long): Note? {
        failWith?.let { throw it }
        return notes.value.find { it.id == id }
    }

    override suspend fun saveNote(
        title: String,
        content: String,
        isPinned: Boolean,
        id: Long,
    ): Long {
        failWith?.let { throw it }
        val existing = notes.value.find { it.id == id }
        return if (existing != null) {
            notes.update { list ->
                list.map {
                    if (it.id == id) it.copy(title = title, content = content, isPinned = isPinned)
                    else it
                }
            }
            id
        } else {
            val newId = nextId++
            notes.update {
                it +
                    Note(
                        id = newId,
                        title = title,
                        content = content,
                        isPinned = isPinned,
                        createdAt = 0,
                        updatedAt = 0,
                    )
            }
            newId
        }
    }

    override suspend fun setPinned(id: Long, isPinned: Boolean) {
        notes.update { list -> list.map { if (it.id == id) it.copy(isPinned = isPinned) else it } }
    }

    override suspend fun deleteNote(id: Long) {
        notes.update { list -> list.filterNot { it.id == id } }
    }
}
