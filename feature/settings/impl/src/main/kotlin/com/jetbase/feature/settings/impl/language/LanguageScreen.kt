package com.jetbase.feature.settings.impl.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.PreferenceSingleChoiceItem
import com.jetbase.feature.settings.impl.R
import java.util.Locale

/**
 * Languages translated in the project. Must match the values-XX folders. When adding a language:
 * create values-XX/strings.xml in every module and add its tag here.
 */
private val SupportedLanguageTags = listOf("en", "es")

/**
 * Per-app language with the official API (AppCompatDelegate.setApplicationLocales). On Android 13+
 * it integrates with the system settings; on older versions AppCompat persists it by itself
 * (autoStoreLocales in the manifest). Seal re-implements this by hand with MMKV for API < 33.
 */
@Composable
internal fun LanguageScreen(onBack: () -> Unit) {
    var current by remember {
        mutableStateOf(AppCompatDelegate.getApplicationLocales().toLanguageTags())
    }
    val select: (String) -> Unit = { tag ->
        current = tag
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }

    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_settings_language),
        onBack = onBack,
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                PreferenceSingleChoiceItem(
                    text = stringResource(R.string.feature_settings_follow_system),
                    selected = current.isEmpty(),
                    onClick = { select("") },
                )
            }
            items(SupportedLanguageTags) { tag ->
                val locale = Locale.forLanguageTag(tag)
                PreferenceSingleChoiceItem(
                    // Each language is shown in ITS OWN language ("English", "Español").
                    text = locale.getDisplayName(locale).replaceFirstChar { it.uppercase(locale) },
                    selected = current == tag,
                    onClick = { select(tag) },
                )
            }
        }
    }
}
