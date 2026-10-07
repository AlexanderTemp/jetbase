package com.jetbase.core.model

/** Every user preference that affects the app, in ONE immutable object. */
data class UserSettings(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FollowSystem,
    /** Use the wallpaper colors (Material You, Android 12+). */
    val useDynamicColor: Boolean = true,
    /** ARGB seed color used when [useDynamicColor] is off. */
    val seedColor: Int = DEFAULT_SEED_COLOR,
    val themeStyle: ThemeStyle = ThemeStyle.TonalSpot,
    /** Pure black background in dark mode (AMOLED screens). */
    val useHighContrast: Boolean = false,
) {
    companion object {
        const val DEFAULT_SEED_COLOR: Int = 0xFF6750A4.toInt()
    }
}

enum class DarkThemeConfig {
    FollowSystem,
    Light,
    Dark,
}

/**
 * Palette style used to generate the color scheme from the seed color.
 *
 * It lives in core:model (not in core:designsystem) because it is a PREFERENCE that gets saved; the
 * design system maps it to the concrete color library.
 */
enum class ThemeStyle {
    TonalSpot,
    Neutral,
    Vibrant,
    Expressive,
    Rainbow,
    FruitSalad,
    Monochrome,
}
