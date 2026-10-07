package com.jetbase.core.datastore

import app.cash.turbine.test
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import com.jetbase.core.testing.datastore.InMemoryKeyValueStore
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UserPreferencesDataSourceTest {
    private val dispatcher = StandardTestDispatcher()
    private val store = InMemoryKeyValueStore()

    @Test
    fun `without saved data it returns the defaults`() {
        val dataSource = UserPreferencesDataSource(store, dispatcher)
        assertEquals(UserSettings(), dataSource.userSettings.value)
    }

    @Test
    fun `changing the dark theme is persisted and emitted`() =
        runTest(dispatcher) {
            val dataSource = UserPreferencesDataSource(store, dispatcher)
            dataSource.userSettings.test {
                assertEquals(DarkThemeConfig.FollowSystem, awaitItem().darkThemeConfig)
                dataSource.setDarkThemeConfig(DarkThemeConfig.Dark)
                assertEquals(DarkThemeConfig.Dark, awaitItem().darkThemeConfig)
            }
            // A NEW instance (like after restarting the app) reads what was persisted.
            val reloaded = UserPreferencesDataSource(store, dispatcher)
            assertEquals(DarkThemeConfig.Dark, reloaded.userSettings.value.darkThemeConfig)
        }

    @Test
    fun `an unknown enum value falls back to the default`() {
        store.putString(UserPreferencesDataSource.Keys.THEME_STYLE, "EstiloQueYaNoExiste")
        val dataSource = UserPreferencesDataSource(store, dispatcher)
        assertEquals(ThemeStyle.TonalSpot, dataSource.userSettings.value.themeStyle)
    }
}
