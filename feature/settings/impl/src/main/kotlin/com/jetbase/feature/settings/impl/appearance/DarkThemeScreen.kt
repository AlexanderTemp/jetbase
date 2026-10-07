package com.jetbase.feature.settings.impl.appearance

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.LoadingContent
import com.jetbase.core.designsystem.component.PreferenceSingleChoiceItem
import com.jetbase.core.designsystem.component.PreferenceSubtitle
import com.jetbase.core.designsystem.component.PreferenceSwitch
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.feature.settings.impl.R
import com.jetbase.feature.settings.impl.SettingsUiState
import com.jetbase.feature.settings.impl.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DarkThemeScreen(onBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_settings_dark_theme),
        onBack = onBack,
    ) { padding ->
        val settings = (uiState as? SettingsUiState.Success)?.settings
        if (settings == null) {
            LoadingContent(Modifier.padding(padding))
            return@JetBaseLargeTopAppBarScaffold
        }
        LazyColumn(contentPadding = padding) {
            items(DarkThemeConfig.entries.size) { index ->
                val config = DarkThemeConfig.entries[index]
                PreferenceSingleChoiceItem(
                    text = config.label(),
                    selected = settings.darkThemeConfig == config,
                    onClick = { viewModel.setDarkThemeConfig(config) },
                )
            }
            item { PreferenceSubtitle(stringResource(R.string.feature_settings_additional)) }
            item {
                PreferenceSwitch(
                    title = stringResource(R.string.feature_settings_high_contrast),
                    description = stringResource(R.string.feature_settings_high_contrast_desc),
                    icon = Icons.Outlined.Star,
                    checked = settings.useHighContrast,
                    onCheckedChange = viewModel::setUseHighContrast,
                )
            }
        }
    }
}

@Composable
internal fun DarkThemeConfig.label(): String =
    stringResource(
        when (this) {
            DarkThemeConfig.FollowSystem -> R.string.feature_settings_follow_system
            DarkThemeConfig.Light -> R.string.feature_settings_light
            DarkThemeConfig.Dark -> R.string.feature_settings_dark
        }
    )
