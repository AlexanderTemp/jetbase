package com.jetbase.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jetbase.core.database.model.NoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * One DAO per entity (Seal has a single VideoInfoDao mixing 4 tables).
 *
 * Reads return Flow: Room re-emits AUTOMATICALLY whenever the table changes.
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY is_pinned DESC, updated_at DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id") fun observeById(id: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE id = :id") suspend fun getById(id: Long): NoteEntity?

    /** Inserts or updates. Returns the rowId when it inserted, or -1 when it updated. */
    @Upsert suspend fun upsert(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :id") suspend fun deleteById(id: Long)
}
