package com.jetbase.core.datastore

import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Preferences data source: translates between primitive keys and the [UserSettings] model.
 *
 * Exposes a [StateFlow] so every observer is notified AUTOMATICALLY of each change, like Seal's
 * `AppSettingsStateFlow`, but without being a global `object`: it is injected and testable.
 */
class UserPreferencesDataSource(
    private val store: KeyValueStore,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val state = MutableStateFlow(readSettings())

    val userSettings: StateFlow<UserSettings> = state.asStateFlow()

    suspend fun setDarkThemeConfig(config: DarkThemeConfig) = edit {
        putString(Keys.DARK_THEME, config.name)
    }

    suspend fun setUseDynamicColor(enabled: Boolean) = edit {
        putBoolean(Keys.DYNAMIC_COLOR, enabled)
    }

    suspend fun setSeedColor(argb: Int) = edit { putInt(Keys.SEED_COLOR, argb) }

    suspend fun setThemeStyle(style: ThemeStyle) = edit { putString(Keys.THEME_STYLE, style.name) }

    suspend fun setUseHighContrast(enabled: Boolean) = edit {
        putBoolean(Keys.HIGH_CONTRAST, enabled)
    }

    /** Writes to disk off the main thread, then publishes the new state. */
    private suspend fun edit(block: KeyValueStore.() -> Unit) {
        withContext(ioDispatcher) {
            store.block()
            state.value = readSettings()
        }
    }

    private fun readSettings(): UserSettings {
        val defaults = UserSettings()
        return UserSettings(
            darkThemeConfig = store.getEnum(Keys.DARK_THEME, defaults.darkThemeConfig),
            useDynamicColor = store.getBoolean(Keys.DYNAMIC_COLOR, defaults.useDynamicColor),
            seedColor = store.getInt(Keys.SEED_COLOR, defaults.seedColor),
            themeStyle = store.getEnum(Keys.THEME_STYLE, defaults.themeStyle),
            useHighContrast = store.getBoolean(Keys.HIGH_CONTRAST, defaults.useHighContrast),
        )
    }

    /**
     * Enums are stored by NAME, not by `ordinal`: reordering or inserting enum values does not
     * corrupt saved preferences (Seal stores Ints with "Do not modify" comments).
     */
    private inline fun <reified T : Enum<T>> KeyValueStore.getEnum(key: String, default: T): T =
        enumValues<T>().firstOrNull { it.name == getString(key, default.name) } ?: default

    internal object Keys {
        const val DARK_THEME = "dark_theme"
        const val DYNAMIC_COLOR = "dynamic_color"
        const val SEED_COLOR = "seed_color"
        const val THEME_STYLE = "theme_style"
        const val HIGH_CONTRAST = "high_contrast"
    }
}
