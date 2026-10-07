package com.jetbase.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

/**
 * Root theme of the app.
 *
 * The design system does NOT know UserSettings or repositories: it takes plain parameters. :app
 * reads the preferences and maps them to these parameters. That way the theme works in @Preview and
 * in any other project without dragging the data layer along.
 *
 * @param useDynamicColor wallpaper colors (Android 12+ only; ignored otherwise).
 * @param seedColor base color used to generate the whole palette when there is no dynamic color.
 * @param highContrast pure black (AMOLED) in dark mode, like Seal's "high contrast".
 */
@Composable
fun JetBaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    seedColor: Color = DefaultSeedColor,
    paletteStyle: PaletteStyle = PaletteStyle.TonalSpot,
    highContrast: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            val scheme =
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkTheme && highContrast) scheme.toAmoled() else scheme
        } else {
            rememberDynamicColorScheme(
                seedColor = seedColor,
                isDark = darkTheme,
                isAmoled = highContrast,
                style = paletteStyle,
                // 2025 spec: the color rules of Material 3 Expressive / Android 16 (the library
                // default is still the 2021 spec).
                specVersion = ColorSpec.SpecVersion.SPEC_2025,
            )
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = JetBaseTypography,
        shapes = JetBaseShapes,
        content = content,
    )
}

/** "Pure black" version of a dark scheme: lower power draw on OLED screens. */
private fun ColorScheme.toAmoled(): ColorScheme =
    copy(
        background = Color.Black,
        surface = Color.Black,
        surfaceContainerLowest = Color.Black,
        surfaceContainerLow = Color.Black,
        surfaceContainer = surfaceContainerLowest,
    )
