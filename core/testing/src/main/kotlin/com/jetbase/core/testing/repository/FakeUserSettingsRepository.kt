package com.jetbase.core.testing.repository

import com.jetbase.core.data.repository.UserSettingsRepository
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserSettingsRepository(initial: UserSettings = UserSettings()) : UserSettingsRepository {
    private val state = MutableStateFlow(initial)

    override val userSettings: Flow<UserSettings> = state

    val current: UserSettings
        get() = state.value

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) = state.update {
        it.copy(darkThemeConfig = config)
    }

    override suspend fun setUseDynamicColor(enabled: Boolean) = state.update {
        it.copy(useDynamicColor = enabled)
    }

    override suspend fun setSeedColor(argb: Int) = state.update { it.copy(seedColor = argb) }

    override suspend fun setThemeStyle(style: ThemeStyle) = state.update {
        it.copy(themeStyle = style)
    }

    override suspend fun setUseHighContrast(enabled: Boolean) = state.update {
        it.copy(useHighContrast = enabled)
    }
}
