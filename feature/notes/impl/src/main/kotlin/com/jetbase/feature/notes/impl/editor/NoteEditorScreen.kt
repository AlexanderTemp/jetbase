package com.jetbase.feature.notes.impl.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbase.core.designsystem.component.BackButton
import com.jetbase.core.designsystem.component.LoadingContent
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.ui.preview.DevicePreviews
import com.jetbase.feature.notes.api.NoteEditorNavKey
import com.jetbase.feature.notes.impl.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun NoteEditorScreen(
    navKey: NoteEditorNavKey,
    onBack: () -> Unit,
    viewModel: NoteEditorViewModel = koinViewModel { parametersOf(navKey) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reacts to the "event as state": when the VM sets isFinished, we navigate back once.
    LaunchedEffect(uiState.isFinished) { if (uiState.isFinished) onBack() }

    NoteEditorContent(
        uiState = uiState,
        onBack = onBack,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onTogglePin = viewModel::onTogglePin,
        onSave = viewModel::onSave,
        onDelete = viewModel::onDelete,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteEditorContent(
    uiState: NoteEditorUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onTogglePin: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (uiState.isNewNote) R.string.feature_notes_new
                            else R.string.feature_notes_edit
                        )
                    )
                },
                navigationIcon = { BackButton(onClick = onBack) },
                actions = {
                    IconButton(onClick = onTogglePin) {
                        Icon(
                            if (uiState.isPinned) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription =
                                stringResource(
                                    if (uiState.isPinned) R.string.feature_notes_unpin
                                    else R.string.feature_notes_pin
                                ),
                        )
                    }
                    if (!uiState.isNewNote) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.feature_notes_delete),
                            )
                        }
                    }
                    IconButton(onClick = onSave, enabled = uiState.canSave) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = stringResource(R.string.feature_notes_save),
                        )
                    }
                },
            )
        },
    ) { padding ->
        if (uiState.isLoading) {
            LoadingContent(Modifier.padding(padding))
            return@Scaffold
        }
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.feature_notes_field_title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = uiState.content,
                onValueChange = onContentChange,
                label = { Text(stringResource(R.string.feature_notes_field_content)) },
                minLines = 8,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@DevicePreviews
@Composable
private fun NoteEditorPreview() {
    JetBaseTheme {
        NoteEditorContent(
            uiState =
                NoteEditorUiState(
                    isLoading = false,
                    isNewNote = false,
                    title = "Ideas",
                    content = "Try the template in a new repo",
                ),
            onBack = {},
            onTitleChange = {},
            onContentChange = {},
            onTogglePin = {},
            onSave = {},
            onDelete = {},
        )
    }
}
