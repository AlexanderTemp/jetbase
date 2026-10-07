package com.jetbase.di

import com.jetbase.MainActivityViewModel
import com.jetbase.core.common.di.commonModule
import com.jetbase.core.data.di.dataModule
import com.jetbase.feature.notes.impl.di.notesFeatureModule
import com.jetbase.feature.settings.impl.di.settingsFeatureModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Root of the dependency graph. To add a new feature: give it its own Koin module and add it to
 * `includes`. That's all.
 */
val appModule = module {
    includes(commonModule, dataModule, notesFeatureModule, settingsFeatureModule)
    viewModelOf(::MainActivityViewModel)
}
