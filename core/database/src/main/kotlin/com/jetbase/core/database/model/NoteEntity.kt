package com.jetbase.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jetbase.core.model.Note

/** How a [Note] looks in SQLite. It only exists inside core:database and core:data. */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    @ColumnInfo(name = "is_pinned") val isPinned: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)

/** Mapper: database layer -> domain model. */
fun NoteEntity.asExternalModel(): Note =
    Note(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

/** Mapper: domain model -> database layer. */
fun Note.asEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
