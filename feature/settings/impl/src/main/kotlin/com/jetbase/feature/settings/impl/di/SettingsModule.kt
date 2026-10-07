package com.jetbase.feature.settings.impl.di

import com.jetbase.feature.settings.impl.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsFeatureModule = module { viewModelOf(::SettingsViewModel) }
