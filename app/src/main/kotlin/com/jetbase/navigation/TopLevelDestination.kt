package com.jetbase.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.jetbase.R
import com.jetbase.feature.notes.api.NotesNavKey
import com.jetbase.feature.settings.api.SettingsNavKey

/**
 * Items of the main navigation bar/rail. When creating a new app from the template this is the
 * first thing to edit: one entry per main section.
 */
enum class TopLevelDestination(
    val key: NavKey,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val label: Int,
) {
    Notes(NotesNavKey, Icons.Filled.Edit, Icons.Outlined.Edit, R.string.nav_notes),
    Settings(SettingsNavKey, Icons.Filled.Settings, Icons.Outlined.Settings, R.string.nav_settings),
}
