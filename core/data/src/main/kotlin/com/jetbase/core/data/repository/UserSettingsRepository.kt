package com.jetbase.core.data.repository

import com.jetbase.core.datastore.UserPreferencesDataSource
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val userSettings: Flow<UserSettings>

    suspend fun setDarkThemeConfig(config: DarkThemeConfig)

    suspend fun setUseDynamicColor(enabled: Boolean)

    suspend fun setSeedColor(argb: Int)

    suspend fun setThemeStyle(style: ThemeStyle)

    suspend fun setUseHighContrast(enabled: Boolean)
}

internal class DefaultUserSettingsRepository(private val dataSource: UserPreferencesDataSource) :
    UserSettingsRepository {
    override val userSettings: Flow<UserSettings> = dataSource.userSettings

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) =
        dataSource.setDarkThemeConfig(config)

    override suspend fun setUseDynamicColor(enabled: Boolean) =
        dataSource.setUseDynamicColor(enabled)

    override suspend fun setSeedColor(argb: Int) = dataSource.setSeedColor(argb)

    override suspend fun setThemeStyle(style: ThemeStyle) = dataSource.setThemeStyle(style)

    override suspend fun setUseHighContrast(enabled: Boolean) =
        dataSource.setUseHighContrast(enabled)
}
