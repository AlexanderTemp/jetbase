package com.jetbase.feature.settings.impl.appearance

import android.os.Build
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbase.core.designsystem.component.ColorSwatch
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.LoadingContent
import com.jetbase.core.designsystem.component.PreferenceItem
import com.jetbase.core.designsystem.component.PreferenceSubtitle
import com.jetbase.core.designsystem.component.PreferenceSwitch
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.designsystem.theme.SeedColorOptions
import com.jetbase.core.model.ThemeStyle
import com.jetbase.core.model.UserSettings
import com.jetbase.core.ui.preview.DevicePreviews
import com.jetbase.core.ui.theme.toPaletteStyle
import com.jetbase.feature.settings.impl.R
import com.jetbase.feature.settings.impl.SettingsUiState
import com.jetbase.feature.settings.impl.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AppearanceScreen(
    onBack: () -> Unit,
    onDarkThemeClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AppearanceContent(
        uiState = uiState,
        onBack = onBack,
        onDarkThemeClick = onDarkThemeClick,
        onDynamicColorChange = viewModel::setUseDynamicColor,
        onSeedColorChange = viewModel::setSeedColor,
        onThemeStyleChange = viewModel::setThemeStyle,
    )
}

@Composable
internal fun AppearanceContent(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    onDarkThemeClick: () -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onSeedColorChange: (Int) -> Unit,
    onThemeStyleChange: (ThemeStyle) -> Unit,
) {
    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_settings_appearance),
        onBack = onBack,
    ) { padding ->
        val settings = (uiState as? SettingsUiState.Success)?.settings
        if (settings == null) {
            LoadingContent(Modifier.padding(padding))
            return@JetBaseLargeTopAppBarScaffold
        }
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState())) {
            PreferenceSubtitle(stringResource(R.string.feature_settings_seed_color))
            Row(
                modifier =
                    Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SeedColorOptions.forEach { color ->
                    ColorSwatch(
                        seedColor = color,
                        style = settings.themeStyle.toPaletteStyle(),
                        selected =
                            !settings.useDynamicColor && settings.seedColor == color.toArgb(),
                        onClick = { onSeedColorChange(color.toArgb()) },
                        contentDescription = stringResource(R.string.feature_settings_seed_color),
                    )
                }
            }

            PreferenceSubtitle(stringResource(R.string.feature_settings_palette_style))
            FlowRow(
                modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ThemeStyle.entries.forEach { style ->
                    FilterChip(
                        selected = !settings.useDynamicColor && settings.themeStyle == style,
                        onClick = { onThemeStyleChange(style) },
                        label = { Text(style.name) },
                    )
                }
            }

            PreferenceSubtitle(stringResource(R.string.feature_settings_general))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PreferenceSwitch(
                    title = stringResource(R.string.feature_settings_dynamic_color),
                    description = stringResource(R.string.feature_settings_dynamic_color_desc),
                    icon = Icons.Outlined.Star,
                    checked = settings.useDynamicColor,
                    onCheckedChange = onDynamicColorChange,
                )
            }
            PreferenceItem(
                title = stringResource(R.string.feature_settings_dark_theme),
                description = settings.darkThemeConfig.label(),
                icon = Icons.Outlined.Settings,
                onClick = onDarkThemeClick,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun AppearancePreview() {
    JetBaseTheme(seedColor = Color(UserSettings.DEFAULT_SEED_COLOR)) {
        AppearanceContent(
            uiState = SettingsUiState.Success(UserSettings(useDynamicColor = false)),
            onBack = {},
            onDarkThemeClick = {},
            onDynamicColorChange = {},
            onSeedColorChange = {},
            onThemeStyleChange = {},
        )
    }
}
