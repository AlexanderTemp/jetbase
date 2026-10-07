package com.jetbase.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jetbase.core.navigation.Navigator
import com.jetbase.feature.settings.api.SettingsNavKey
import com.jetbase.feature.settings.impl.about.AboutScreen
import com.jetbase.feature.settings.impl.appearance.AppearanceScreen
import com.jetbase.feature.settings.impl.appearance.DarkThemeScreen
import com.jetbase.feature.settings.impl.home.SettingsHomeScreen
import com.jetbase.feature.settings.impl.language.LanguageScreen
import kotlinx.serialization.Serializable

// Private keys: only this module can navigate to these sub-screens.
@Serializable internal data object AppearanceNavKey : NavKey

@Serializable internal data object DarkThemeNavKey : NavKey

@Serializable internal data object LanguageNavKey : NavKey

@Serializable internal data object AboutNavKey : NavKey

fun EntryProviderScope<NavKey>.settingsEntries(navigator: Navigator) {
    val onBack: () -> Unit = { navigator.goBack() }

    entry<SettingsNavKey> { SettingsHomeScreen(onNavigate = navigator::navigate) }
    entry<AppearanceNavKey> {
        AppearanceScreen(
            onBack = onBack,
            onDarkThemeClick = { navigator.navigate(DarkThemeNavKey) },
        )
    }
    entry<DarkThemeNavKey> { DarkThemeScreen(onBack = onBack) }
    entry<LanguageNavKey> { LanguageScreen(onBack = onBack) }
    entry<AboutNavKey> { AboutScreen(onBack = onBack) }
}
