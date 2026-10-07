package com.jetbase.feature.settings.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Public navigation API of the settings feature: only its entry point. The sub-screens (appearance,
 * dark theme, language, about) are private to :impl because nobody else needs them.
 */
@Serializable data object SettingsNavKey : NavKey
