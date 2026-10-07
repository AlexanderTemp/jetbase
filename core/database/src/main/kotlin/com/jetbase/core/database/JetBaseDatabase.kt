package com.jetbase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jetbase.core.database.dao.NoteDao
import com.jetbase.core.database.model.NoteEntity

/**
 * When you change the schema: bump `version`, build (schemas/N.json is generated) and add
 * `autoMigrations = [AutoMigration(from = N-1, to = N)]`, just like Seal does with its 5 versions.
 */
@Database(entities = [NoteEntity::class], version = 1, exportSchema = true)
abstract class JetBaseDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
