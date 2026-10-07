package com.jetbase.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbase.core.data.repository.UserSettingsRepository
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.ThemeStyle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * One ViewModel for all the appearance settings screens. Each screen gets its own instance, but
 * they all read from the SAME repository (single source of truth), so they never get out of sync.
 */
class SettingsViewModel(private val repository: UserSettingsRepository) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        repository.userSettings
            .map<_, SettingsUiState> { SettingsUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState.Loading,
            )

    fun setDarkThemeConfig(config: DarkThemeConfig) = launch {
        repository.setDarkThemeConfig(config)
    }

    fun setUseDynamicColor(enabled: Boolean) = launch { repository.setUseDynamicColor(enabled) }

    /** Picking a seed color implies turning dynamic color off (like Seal does). */
    fun setSeedColor(argb: Int) = launch {
        repository.setUseDynamicColor(false)
        repository.setSeedColor(argb)
    }

    fun setThemeStyle(style: ThemeStyle) = launch {
        repository.setUseDynamicColor(false)
        repository.setThemeStyle(style)
    }

    fun setUseHighContrast(enabled: Boolean) = launch { repository.setUseHighContrast(enabled) }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}
