package com.jetbase.core.datastore.di

import com.jetbase.core.common.di.Qualifiers
import com.jetbase.core.datastore.KeyValueStore
import com.jetbase.core.datastore.MmkvKeyValueStore
import com.jetbase.core.datastore.UserPreferencesDataSource
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {
    single<KeyValueStore> {
        // Lazy initialization: MMKV is initialized the first time somebody asks for it,
        // not in Application.onCreate (Seal did it by hand in App).
        MMKV.initialize(androidContext())
        MmkvKeyValueStore(MMKV.mmkvWithID("user_preferences"))
    }
    single { UserPreferencesDataSource(store = get(), ioDispatcher = get(Qualifiers.IoDispatcher)) }
}
