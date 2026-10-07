package com.jetbase.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * The whole navigation state of the app, as plain Compose state that WE own.
 *
 * With Navigation 3 there is no hidden NavController: the back stack is a list of keys. To support
 * bottom navigation with one history per tab we keep:
 * - [topLevelStack]: the order in which tabs were visited (always starts with [startKey]);
 * - [subStacks]: one back stack per tab, each starting with that tab's key.
 *
 * Both are created with `rememberNavBackStack`, so they survive rotation AND process death (keys
 * are `@Serializable`).
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val topLevelKeys: Set<NavKey>
        get() = subStacks.keys

    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks.getValue(currentTopLevelKey)

    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }

    /** True when the user is on the root screen of a tab (where the navigation bar is shown). */
    val isOnTopLevelScreen: Boolean by derivedStateOf { currentKey in topLevelKeys }
}

@Composable
fun rememberNavigationState(startKey: NavKey, topLevelKeys: Set<NavKey>): NavigationState {
    require(startKey in topLevelKeys) { "startKey must be one of the topLevelKeys" }
    val topLevelStack = rememberNavBackStack(startKey)
    // The set of tabs is fixed for the lifetime of the app, so calling remember* in a loop is safe.
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }
    return remember(startKey, topLevelKeys) { NavigationState(startKey, topLevelStack, subStacks) }
}

/**
 * Flattens the state into the list of entries that `NavDisplay` renders: the sub stacks of the
 * visited tabs, in visiting order.
 *
 * Each sub stack is decorated with:
 * - a SaveableStateHolder: `rememberSaveable` state survives while the entry is in the stack;
 * - a ViewModelStore: each entry gets its OWN ViewModels, cleared when the entry is popped.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
    val decoratedEntries = subStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider = entryProvider,
        )
    }
    return topLevelStack.flatMap { decoratedEntries[it].orEmpty() }
}
