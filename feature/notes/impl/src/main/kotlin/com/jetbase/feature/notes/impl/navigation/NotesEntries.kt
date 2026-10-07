package com.jetbase.feature.notes.impl.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jetbase.core.designsystem.component.MessageContent
import com.jetbase.core.navigation.Navigator
import com.jetbase.feature.notes.api.NoteEditorNavKey
import com.jetbase.feature.notes.api.NotesNavKey
import com.jetbase.feature.notes.api.navigateToNoteEditor
import com.jetbase.feature.notes.impl.R
import com.jetbase.feature.notes.impl.editor.NoteEditorScreen
import com.jetbase.feature.notes.impl.list.NotesScreen

/**
 * Registers the screens of this feature in the app's NavDisplay.
 *
 * The metadata marks the list as the "list pane" and the editor as the "detail pane": on a phone
 * they are shown one at a time; on a tablet/foldable the ListDetailSceneStrategy shows them side by
 * side automatically. Same code, adaptive result.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.notesEntries(navigator: Navigator) {
    entry<NotesNavKey>(
        metadata =
            ListDetailSceneStrategy.listPane(
                detailPlaceholder = {
                    MessageContent(
                        icon = Icons.Outlined.Edit,
                        title = stringResource(R.string.feature_notes_select_note),
                    )
                }
            )
    ) {
        NotesScreen(
            onNoteClick = navigator::navigateToNoteEditor,
            onCreateNote = { navigator.navigateToNoteEditor() },
        )
    }
    entry<NoteEditorNavKey>(metadata = ListDetailSceneStrategy.detailPane()) { key ->
        NoteEditorScreen(navKey = key, onBack = { navigator.goBack() })
    }
}
