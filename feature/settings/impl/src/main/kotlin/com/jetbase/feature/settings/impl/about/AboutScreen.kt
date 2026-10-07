package com.jetbase.feature.settings.impl.about

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jetbase.core.designsystem.component.JetBaseLargeTopAppBarScaffold
import com.jetbase.core.designsystem.component.PreferenceItem
import com.jetbase.feature.settings.impl.R

@Composable
internal fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val versionName =
        remember(context) {
            val pm = context.packageManager
            val info =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
                } else {
                    @Suppress("DEPRECATION") pm.getPackageInfo(context.packageName, 0)
                }
            info.versionName.orEmpty()
        }

    JetBaseLargeTopAppBarScaffold(
        title = stringResource(R.string.feature_settings_about),
        onBack = onBack,
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                PreferenceItem(
                    title = stringResource(R.string.feature_settings_version),
                    description = versionName,
                    icon = Icons.Outlined.Info,
                )
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.feature_settings_package),
                    description = context.packageName,
                )
            }
        }
    }
}
