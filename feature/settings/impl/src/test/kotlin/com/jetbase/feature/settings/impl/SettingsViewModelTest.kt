package com.jetbase.feature.settings.impl

import app.cash.turbine.test
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import com.jetbase.core.testing.repository.FakeUserSettingsRepository
import com.jetbase.core.testing.rules.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeUserSettingsRepository()

    // CAREFUL: the ViewModel is created INSIDE each test (or with `by lazy`), never as a directly
    // initialized property: that happens BEFORE MainDispatcherRule swaps Dispatchers.Main, and the
    // ViewModel's stateIn would stay attached to the wrong dispatcher.
    private val viewModel by lazy { SettingsViewModel(repository) }

    @Test
    fun `exposes the repository settings`() = runTest {
        viewModel.uiState.test {
            assertEquals(SettingsUiState.Success(UserSettings()), awaitItem())
            viewModel.setDarkThemeConfig(DarkThemeConfig.Dark)
            val state = awaitItem() as SettingsUiState.Success
            assertEquals(DarkThemeConfig.Dark, state.settings.darkThemeConfig)
        }
    }

    @Test
    fun `picking a seed color or style turns dynamic color off`() {
        viewModel.setSeedColor(0xFF00FF00.toInt())
        assertFalse(repository.current.useDynamicColor)
        assertEquals(0xFF00FF00.toInt(), repository.current.seedColor)

        viewModel.setUseDynamicColor(true)
        viewModel.setThemeStyle(ThemeStyle.Vibrant)
        assertFalse(repository.current.useDynamicColor)
        assertEquals(ThemeStyle.Vibrant, repository.current.themeStyle)
    }
}
