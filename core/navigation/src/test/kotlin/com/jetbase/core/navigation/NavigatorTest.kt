package com.jetbase.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private data object Home : NavKey

private data object Settings : NavKey

private data class Detail(val id: Int) : NavKey

class NavigatorTest {
    // NavBackStack is plain snapshot state: it can be built directly in a JVM test.
    private val state =
        NavigationState(
            startKey = Home,
            topLevelStack = NavBackStack(Home),
            subStacks = mapOf(Home to NavBackStack(Home), Settings to NavBackStack(Settings)),
        )
    private val navigator = Navigator(state)

    @Test
    fun `pushing a key adds it to the current tab`() {
        navigator.navigate(Detail(1))
        assertEquals(Detail(1), state.currentKey)
        assertFalse(state.isOnTopLevelScreen)
    }

    @Test
    fun `each tab keeps its own history`() {
        navigator.navigate(Detail(1))
        navigator.navigate(Settings)
        assertEquals(Settings, state.currentKey)

        navigator.navigate(Home)
        assertEquals(Detail(1), state.currentKey)
    }

    @Test
    fun `navigating to the current tab resets it`() {
        navigator.navigate(Detail(1))
        navigator.navigate(Detail(2))
        navigator.navigate(Home)
        assertEquals(Home, state.currentKey)
    }

    @Test
    fun `back pops the sub stack, then the tab, then reports false`() {
        navigator.navigate(Settings)
        navigator.navigate(Detail(1))

        assertTrue(navigator.goBack())
        assertEquals(Settings, state.currentKey)
        assertTrue(navigator.goBack())
        assertEquals(Home, state.currentKey)
        assertFalse(navigator.goBack())
    }

    @Test
    fun `a key is never duplicated in the stack`() {
        navigator.navigate(Detail(1))
        navigator.navigate(Detail(2))
        navigator.navigate(Detail(1))
        assertEquals(listOf(Home, Detail(2), Detail(1)), state.currentSubStack.toList())
    }
}
