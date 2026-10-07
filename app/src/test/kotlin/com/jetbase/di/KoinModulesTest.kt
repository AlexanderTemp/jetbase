package com.jetbase.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.jetbase.feature.notes.api.NoteEditorNavKey
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

/**
 * Checks in a JVM test (milliseconds) that the WHOLE Koin graph can be built: if someone adds a
 * dependency to a ViewModel constructor and forgets to declare it, this test fails in CI instead of
 * the app crashing at runtime. It is the "safety net" that Hilt gives you at compile time.
 */
@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest {
    @Test
    fun `the whole dependency graph is valid`() {
        appModule.verify(
            // Types provided by Android at runtime that no module declares.
            extraTypes = listOf(Context::class, SavedStateHandle::class),
            injections =
                injectedParameters(
                    definition<com.jetbase.feature.notes.impl.editor.NoteEditorViewModel>(
                        NoteEditorNavKey::class
                    )
                ),
        )
    }
}
