package com.jetbase.core.database.di

import androidx.room.Room
import com.jetbase.core.database.JetBaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), JetBaseDatabase::class.java, "jetbase.db").build()
    }
    single { get<JetBaseDatabase>().noteDao() }
}
