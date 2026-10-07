package com.jetbase.feature.notes.impl.list

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.model.Note
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI test of the STATELESS content: no ViewModel, no Koin, no database. This is why
 * the Screen/Content split matters. Run with `./gradlew
 * :feature:notes:impl:connectedDebugAndroidTest`.
 */
@RunWith(AndroidJUnit4::class)
class NotesContentTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun emptyState_showsMessage_andAccessibleCreateButton() {
        var created = 0
        composeRule.setContent {
            JetBaseTheme {
                NotesContent(
                    uiState = NotesUiState.Empty,
                    onNoteClick = {},
                    onCreateNote = { created++ },
                    onTogglePin = { _, _ -> },
                )
            }
        }

        composeRule.onNodeWithText("No notes yet").assertIsDisplayed()
        // The merged semantics tree is what TalkBack reads. ExtendedFloatingActionButton clears its
        // text semantics, so the button must be labelled through the icon's contentDescription.
        composeRule
            .onNodeWithContentDescription("New note")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        assertEquals(1, created)
    }

    @Test
    fun pinButton_reportsTheNoteAndItsState() {
        var toggled: Pair<Long, Boolean>? = null
        composeRule.setContent {
            JetBaseTheme {
                NotesContent(
                    uiState = NotesUiState.Success(listOf(Note(7, "Ideas", "", false, 0, 0))),
                    onNoteClick = {},
                    onCreateNote = {},
                    onTogglePin = { id, pinned -> toggled = id to pinned },
                )
            }
        }

        composeRule.onNodeWithText("Ideas").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Pin").performClick()
        assertEquals(7L to false, toggled)
    }
}
