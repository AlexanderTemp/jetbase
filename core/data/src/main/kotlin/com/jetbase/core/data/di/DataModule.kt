package com.jetbase.core.data.di

import com.jetbase.core.common.di.Qualifiers
import com.jetbase.core.data.repository.DefaultUserSettingsRepository
import com.jetbase.core.data.repository.NotesRepository
import com.jetbase.core.data.repository.OfflineNotesRepository
import com.jetbase.core.data.repository.UserSettingsRepository
import com.jetbase.core.database.di.databaseModule
import com.jetbase.core.datastore.di.datastoreModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Data module. It includes the database and datastore modules: :app only knows `dataModule` and
 * does not need to know that Room or MMKV live underneath.
 */
val dataModule = module {
    includes(databaseModule, datastoreModule)

    single<NotesRepository> {
        OfflineNotesRepository(
            noteDao = get(),
            ioDispatcher = get(Qualifiers.IoDispatcher),
            timeProvider = get(),
        )
    }
    singleOf(::DefaultUserSettingsRepository) bind UserSettingsRepository::class
}
