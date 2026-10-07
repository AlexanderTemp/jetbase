package com.jetbase.feature.notes.impl.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.LoadingContent
import com.jetbase.core.designsystem.component.MessageContent
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.model.Note
import com.jetbase.core.ui.preview.DevicePreviews
import com.jetbase.feature.notes.impl.R
import org.koin.androidx.compose.koinViewModel

/**
 * STATEFUL composable: gets the ViewModel and observes its state. It is the only one that knows
 * about Koin and lifecycle. It cannot be previewed (it needs a real ViewModel).
 */
@Composable
internal fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onCreateNote: () -> Unit,
    viewModel: NotesViewModel = koinViewModel(),
) {
    // collectAsStateWithLifecycle stops collecting when the app goes to the background.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotesContent(
        uiState = uiState,
        onNoteClick = onNoteClick,
        onCreateNote = onCreateNote,
        onTogglePin = viewModel::onTogglePin,
    )
}

/**
 * STATELESS composable: it only receives data and lambdas. It can be previewed and tested without a
 * ViewModel or Koin. 95% of the UI code lives here.
 */
@Composable
internal fun NotesContent(
    uiState: NotesUiState,
    onNoteClick: (Long) -> Unit,
    onCreateNote: () -> Unit,
    onTogglePin: (noteId: Long, isPinned: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_notes_title),
        onBack = null,
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNote,
                // ExtendedFloatingActionButton CLEARS the semantics of its text: the accessible
                // label must be the icon's contentDescription (otherwise TalkBack only says
                // "Button"). Caught by NotesContentTest on a real emulator.
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.feature_notes_new),
                    )
                },
                text = { Text(stringResource(R.string.feature_notes_new)) },
            )
        },
    ) { padding ->
        when (uiState) {
            NotesUiState.Loading -> LoadingContent(Modifier.padding(padding))
            NotesUiState.Empty ->
                MessageContent(
                    icon = Icons.Outlined.Edit,
                    title = stringResource(R.string.feature_notes_empty_title),
                    description = stringResource(R.string.feature_notes_empty_description),
                    modifier = Modifier.padding(padding),
                )
            is NotesUiState.Error ->
                MessageContent(
                    icon = Icons.Outlined.Warning,
                    title = stringResource(R.string.feature_notes_error_title),
                    description = uiState.message,
                    modifier = Modifier.padding(padding),
                )
            is NotesUiState.Success ->
                NotesGrid(
                    notes = uiState.notes,
                    contentPadding = padding,
                    onNoteClick = onNoteClick,
                    onTogglePin = onTogglePin,
                )
        }
    }
}

@Composable
private fun NotesGrid(
    notes: List<Note>,
    contentPadding: PaddingValues,
    onNoteClick: (Long) -> Unit,
    onTogglePin: (Long, Boolean) -> Unit,
) {
    // Adaptive: 1 column on phones, several on tablets, with no extra code.
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 96.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Stable key: Compose reuses the right item when the list reorders (pin/unpin).
        items(notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                onClick = { onNoteClick(note.id) },
                onTogglePin = { onTogglePin(note.id, note.isPinned) },
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (note.isPinned) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh
            ),
    ) {
        Column(Modifier.padding(start = 16.dp, bottom = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = note.title.ifBlank { stringResource(R.string.feature_notes_untitled) },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(top = 12.dp),
                )
                IconButton(onClick = onTogglePin) {
                    Icon(
                        imageVector =
                            if (note.isPinned) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                        contentDescription =
                            stringResource(
                                if (note.isPinned) R.string.feature_notes_unpin
                                else R.string.feature_notes_pin
                            ),
                    )
                }
            }
            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun NotesContentPreview() {
    JetBaseTheme {
        NotesContent(
            uiState =
                NotesUiState.Success(
                    listOf(
                        Note(1, "Shopping list", "Milk\nBread", true, 0, 0),
                        Note(2, "Ideas", "Try Navigation 3 in the template", false, 0, 0),
                    )
                ),
            onNoteClick = {},
            onCreateNote = {},
            onTogglePin = { _, _ -> },
        )
    }
}
