package com.jetbase.core.model

/**
 * Domain model of a note. It is the ONLY "Note" type the UI and ViewModels see.
 *
 * It has no Room or serialization annotations: those belong to their own layers (NoteEntity in
 * core:database). That is why this module is pure Kotlin.
 */
data class Note(
    val id: Long = NEW_NOTE_ID,
    val title: String,
    val content: String,
    val isPinned: Boolean = false,
    /** Epoch millis. */
    val createdAt: Long,
    /** Epoch millis. */
    val updatedAt: Long,
) {
    val isBlank: Boolean
        get() = title.isBlank() && content.isBlank()

    companion object {
        /** Id meaning "note not saved yet" (Room generates the real one). */
        const val NEW_NOTE_ID = 0L
    }
}
