package com.jetbase.core.data.repository

import app.cash.turbine.test
import com.jetbase.core.database.dao.NoteDao
import com.jetbase.core.database.model.NoteEntity
import com.jetbase.core.model.Note
import com.jetbase.core.testing.time.TestTimeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OfflineNotesRepositoryTest {
    private val dao = FakeNoteDao()
    private val time = TestTimeProvider(start = 1_000)
    private val repository =
        OfflineNotesRepository(
            noteDao = dao,
            ioDispatcher = UnconfinedTestDispatcher(),
            timeProvider = time,
        )

    @Test
    fun `creating a note assigns id and timestamps`() = runTest {
        val id = repository.saveNote("  Hola ", "Mundo", isPinned = false, id = Note.NEW_NOTE_ID)

        val note = repository.getNote(id)!!
        assertEquals("Hola", note.title) // surrounding spaces are trimmed
        assertEquals(1_000, note.createdAt)
        assertEquals(1_000, note.updatedAt)
    }

    @Test
    fun `updating keeps createdAt and changes updatedAt`() = runTest {
        val id = repository.saveNote("A", "", isPinned = false, id = Note.NEW_NOTE_ID)
        time.advanceBy(500)

        val sameId = repository.saveNote("B", "", isPinned = true, id = id)

        val note = repository.getNote(id)!!
        assertEquals(id, sameId)
        assertEquals("B", note.title)
        assertEquals(1_000, note.createdAt)
        assertEquals(1_500, note.updatedAt)
    }

    @Test
    fun `observeNotes emits changes and pinned notes come first`() = runTest {
        repository.observeNotes().test {
            assertEquals(emptyList<Note>(), awaitItem())
            repository.saveNote("normal", "", isPinned = false, id = Note.NEW_NOTE_ID)
            awaitItem()
            repository.saveNote("fijada", "", isPinned = true, id = Note.NEW_NOTE_ID)
            assertEquals(listOf("fijada", "normal"), awaitItem().map { it.title })
        }
    }

    @Test
    fun `deleting removes the note`() = runTest {
        val id = repository.saveNote("x", "", isPinned = false, id = Note.NEW_NOTE_ID)
        repository.deleteNote(id)
        assertNull(repository.getNote(id))
    }
}

/** In-memory fake DAO: mirrors Room's contract without SQLite (instant JVM test). */
private class FakeNoteDao : NoteDao {
    private val notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    private var nextId = 1L

    override fun observeAll(): Flow<List<NoteEntity>> = notes.map { list ->
        list.sortedWith(
            compareByDescending<NoteEntity> { it.isPinned }.thenByDescending { it.updatedAt }
        )
    }

    override fun observeById(id: Long): Flow<NoteEntity?> = notes.map { list ->
        list.find { it.id == id }
    }

    override suspend fun getById(id: Long): NoteEntity? = notes.value.find { it.id == id }

    override suspend fun upsert(note: NoteEntity): Long {
        if (note.id != 0L && notes.value.any { it.id == note.id }) {
            notes.update { list -> list.map { if (it.id == note.id) note else it } }
            return -1
        }
        val id = nextId++
        notes.update { it + note.copy(id = id) }
        return id
    }

    override suspend fun deleteById(id: Long) {
        notes.update { list -> list.filterNot { it.id == id } }
    }
}
