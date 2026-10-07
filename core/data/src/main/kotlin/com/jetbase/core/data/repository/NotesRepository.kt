package com.jetbase.core.data.repository

import com.jetbase.core.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Public contract to access notes. ViewModels depend on THIS interface, never on the
 * implementation, so it can be replaced by a fake in tests.
 */
interface NotesRepository {
    fun observeNotes(): Flow<List<Note>>

    fun observeNote(id: Long): Flow<Note?>

    suspend fun getNote(id: Long): Note?

    /**
     * Creates the note when its id is [Note.NEW_NOTE_ID], or updates it when it already exists.
     *
     * @return the final id of the note.
     */
    suspend fun saveNote(title: String, content: String, isPinned: Boolean, id: Long): Long

    suspend fun setPinned(id: Long, isPinned: Boolean)

    suspend fun deleteNote(id: Long)
}
