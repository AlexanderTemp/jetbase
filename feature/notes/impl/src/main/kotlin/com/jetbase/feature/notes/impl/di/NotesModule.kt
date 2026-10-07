package com.jetbase.feature.notes.impl.di

import com.jetbase.feature.notes.impl.editor.NoteEditorViewModel
import com.jetbase.feature.notes.impl.list.NotesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Each feature publishes ITS Koin module; :app puts them all together. */
val notesFeatureModule = module {
    viewModelOf(::NotesViewModel)
    // `params.get()` receives the NoteEditorNavKey passed with parametersOf(navKey).
    viewModel { params -> NoteEditorViewModel(navKey = params.get(), notesRepository = get()) }
}
