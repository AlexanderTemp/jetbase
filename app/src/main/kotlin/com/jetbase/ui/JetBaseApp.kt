package com.jetbase.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.jetbase.core.navigation.Navigator
import com.jetbase.core.navigation.rememberNavigationState
import com.jetbase.core.navigation.toEntries
import com.jetbase.core.ui.navigation.NavTransitions
import com.jetbase.feature.notes.api.NotesNavKey
import com.jetbase.feature.notes.impl.navigation.notesEntries
import com.jetbase.feature.settings.impl.navigation.settingsEntries
import com.jetbase.navigation.TopLevelDestination

/**
 * The visual skeleton of the app: adaptive navigation + NavDisplay (Navigation 3).
 *
 * - NavigationSuiteScaffold picks bottom bar / rail / drawer from the window size.
 * - NavDisplay renders the back stack; the ListDetailSceneStrategy shows list + detail side by side
 *   on large screens for the entries that declare list/detail metadata.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun JetBaseApp() {
    val navigationState =
        rememberNavigationState(
            startKey = NotesNavKey,
            topLevelKeys = TopLevelDestination.entries.map { it.key }.toSet(),
        )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val layoutType =
        if (navigationState.isOnTopLevelScreen) {
            NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())
        } else NavigationSuiteType.None

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                val selected = destination.key == navigationState.currentTopLevelKey
                item(
                    selected = selected,
                    onClick = { navigator.navigate(destination.key) },
                    icon = {
                        Icon(
                            if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                )
            }
        },
    ) {
        val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
        // Each feature contributes its entries. Adding a feature = one more line here.
        val entryProvider = entryProvider {
            notesEntries(navigator)
            settingsEntries(navigator)
        }
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            sceneStrategies = listOf(listDetailStrategy),
            transitionSpec = NavTransitions.forward(),
            popTransitionSpec = NavTransitions.pop(),
            predictivePopTransitionSpec = NavTransitions.predictivePop(),
            onBack = { navigator.goBack() },
        )
    }
}
