package com.jetbase.feature.settings.impl

import com.jetbase.core.model.UserSettings

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val settings: UserSettings) : SettingsUiState
}
