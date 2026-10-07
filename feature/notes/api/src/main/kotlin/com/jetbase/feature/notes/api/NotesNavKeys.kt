package com.jetbase.feature.notes.api

import androidx.navigation3.runtime.NavKey
import com.jetbase.core.model.Note
import com.jetbase.core.navigation.Navigator
import kotlinx.serialization.Serializable

/*
 * Public navigation API of the notes feature. Anything that wants to open a notes screen (the app,
 * or ANOTHER feature) depends only on this small :api module, never on :impl.
 */

/** The notes list. Top-level destination (a tab). */
@Serializable data object NotesNavKey : NavKey

/** The note editor. Arguments are typed properties: impossible to misspell or mistype. */
@Serializable data class NoteEditorNavKey(val noteId: Long = Note.NEW_NOTE_ID) : NavKey

fun Navigator.navigateToNoteEditor(noteId: Long = Note.NEW_NOTE_ID) =
    navigate(NoteEditorNavKey(noteId))
