package com.jetbase.feature.settings.impl.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.PreferenceItem
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.ui.preview.DevicePreviews
import com.jetbase.feature.settings.impl.R
import com.jetbase.feature.settings.impl.navigation.AboutNavKey
import com.jetbase.feature.settings.impl.navigation.AppearanceNavKey
import com.jetbase.feature.settings.impl.navigation.LanguageNavKey

/**
 * Settings "index" screen. It has no state of its own: it only links to sub-screens. When creating
 * a new repo, add here the entries for that app's specific settings.
 */
@Composable
internal fun SettingsHomeScreen(onNavigate: (NavKey) -> Unit) {
    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_settings_title),
        onBack = null,
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                PreferenceItem(
                    title = stringResource(R.string.feature_settings_appearance),
                    description = stringResource(R.string.feature_settings_appearance_desc),
                    icon = Icons.Outlined.Star,
                    onClick = { onNavigate(AppearanceNavKey) },
                )
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.feature_settings_language),
                    description = stringResource(R.string.feature_settings_language_desc),
                    icon = Icons.Outlined.Settings,
                    onClick = { onNavigate(LanguageNavKey) },
                )
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.feature_settings_about),
                    description = stringResource(R.string.feature_settings_about_desc),
                    icon = Icons.Outlined.Info,
                    onClick = { onNavigate(AboutNavKey) },
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun SettingsHomePreview() {
    JetBaseTheme { SettingsHomeScreen(onNavigate = {}) }
}
