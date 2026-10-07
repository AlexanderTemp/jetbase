package com.jetbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbase.core.data.repository.UserSettingsRepository
import com.jetbase.core.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Exposes the preferences that affect the WHOLE app (theme). */
class MainActivityViewModel(userSettingsRepository: UserSettingsRepository) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        userSettingsRepository.userSettings
            .map<_, MainActivityUiState> { MainActivityUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainActivityUiState.Loading,
            )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val settings: UserSettings) : MainActivityUiState
}
