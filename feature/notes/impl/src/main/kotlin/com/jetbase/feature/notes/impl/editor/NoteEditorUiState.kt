package com.jetbase.feature.notes.impl.editor

/**
 * Editor state. A data class (not sealed) fits better here: the form always exists, only its fields
 * change.
 *
 * [isFinished] is an EVENT modelled as state: when it becomes true the UI navigates back. This is
 * Google's official recommendation over Channels/SharedFlow for one-off events.
 */
data class NoteEditorUiState(
    val isLoading: Boolean = true,
    val isNewNote: Boolean = true,
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isFinished: Boolean = false,
) {
    val canSave: Boolean
        get() = !isLoading && (title.isNotBlank() || content.isNotBlank())
}
