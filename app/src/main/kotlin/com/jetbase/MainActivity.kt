package com.jetbase

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbase.core.designsystem.theme.JetBaseTheme
import com.jetbase.core.model.DarkThemeConfig
import com.jetbase.core.model.UserSettings
import com.jetbase.core.ui.theme.toPaletteStyle
import com.jetbase.ui.JetBaseApp
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * AppCompatActivity (not ComponentActivity) because AppCompatDelegate.setApplicationLocales needs
 * an AppCompatActivity to apply the per-app language on Android < 13.
 */
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // The splash stays until the preferences are loaded, so the user never sees a light
        // theme "flash" before the dark one is applied.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is MainActivityUiState.Loading
        }

        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val settings = (uiState as? MainActivityUiState.Success)?.settings ?: UserSettings()
            val darkTheme = settings.shouldUseDarkTheme()

            // Light/dark status bar icons follow the theme the user CHOSE, not the system one.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle =
                        SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(LightScrim, DarkScrim) { darkTheme },
                )
                onDispose {}
            }

            JetBaseTheme(
                darkTheme = darkTheme,
                useDynamicColor = settings.useDynamicColor,
                seedColor = androidx.compose.ui.graphics.Color(settings.seedColor),
                paletteStyle = settings.themeStyle.toPaletteStyle(),
                highContrast = settings.useHighContrast,
            ) {
                JetBaseApp()
            }
        }
    }
}

@Composable
private fun UserSettings.shouldUseDarkTheme(): Boolean =
    when (darkThemeConfig) {
        DarkThemeConfig.FollowSystem -> isSystemInDarkTheme()
        DarkThemeConfig.Light -> false
        DarkThemeConfig.Dark -> true
    }

private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
