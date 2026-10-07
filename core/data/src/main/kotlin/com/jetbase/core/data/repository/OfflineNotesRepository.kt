package com.jetbase.core.data.repository

import com.jetbase.core.common.time.TimeProvider
import com.jetbase.core.database.dao.NoteDao
import com.jetbase.core.database.model.NoteEntity
import com.jetbase.core.database.model.asExternalModel
import com.jetbase.core.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Local implementation (Room is the single source of truth).
 *
 * If a derived repo adds a network, this class becomes "offline-first": the UI keeps reading ALWAYS
 * from Room and the network only syncs into Room.
 */
internal class OfflineNotesRepository(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> =
        noteDao.observeAll().map { entities -> entities.map(NoteEntity::asExternalModel) }

    override fun observeNote(id: Long): Flow<Note?> =
        noteDao.observeById(id).map { it?.asExternalModel() }

    override suspend fun getNote(id: Long): Note? =
        withContext(ioDispatcher) { noteDao.getById(id)?.asExternalModel() }

    override suspend fun saveNote(
        title: String,
        content: String,
        isPinned: Boolean,
        id: Long,
    ): Long =
        withContext(ioDispatcher) {
            val now = timeProvider.now()
            val existing = if (id == Note.NEW_NOTE_ID) null else noteDao.getById(id)
            val entity =
                NoteEntity(
                    id = existing?.id ?: 0,
                    title = title.trim(),
                    content = content.trim(),
                    isPinned = isPinned,
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now,
                )
            val rowId = noteDao.upsert(entity)
            // @Upsert returns -1 when it updates: in that case the id is the one we already had.
            if (existing == null) rowId else existing.id
        }

    override suspend fun setPinned(id: Long, isPinned: Boolean) =
        withContext(ioDispatcher) {
            val entity = noteDao.getById(id) ?: return@withContext
            noteDao.upsert(entity.copy(isPinned = isPinned))
            Unit
        }

    override suspend fun deleteNote(id: Long) = withContext(ioDispatcher) { noteDao.deleteById(id) }
}
