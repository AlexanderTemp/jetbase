package com.jetbase.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * The ONLY object that mutates [NavigationState]. Features receive a Navigator and call
 * `navigate(key)` / `goBack()`; they never touch the back stack lists directly.
 *
 * Rules (the standard Material bottom-navigation behaviour):
 * - navigating to the current tab resets it to its root screen;
 * - navigating to another tab switches to it and restores its own history;
 * - any other key is pushed onto the current tab's stack.
 */
class Navigator(val state: NavigationState) {

    fun navigate(key: NavKey) {
        when (key) {
            state.currentTopLevelKey -> resetCurrentTab()
            in state.topLevelKeys -> switchTab(key)
            else -> push(key)
        }
    }

    /** @return false when already on the start screen (the system should then close the app). */
    fun goBack(): Boolean {
        val subStack = state.currentSubStack
        return when {
            subStack.size > 1 -> {
                subStack.removeLastOrNull()
                true
            }
            state.topLevelStack.size > 1 -> {
                state.topLevelStack.removeLastOrNull()
                true
            }
            else -> false
        }
    }

    private fun push(key: NavKey) {
        state.currentSubStack.apply {
            // A key appears at most once: navigating to it again moves it to the top.
            remove(key)
            add(key)
        }
    }

    private fun switchTab(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) clear() else remove(key)
            add(key)
        }
    }

    private fun resetCurrentTab() {
        state.currentSubStack.apply { if (size > 1) subList(1, size).clear() }
    }
}
