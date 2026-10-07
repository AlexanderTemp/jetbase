package com.jetbase.core.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview: a single annotation renders the preview in light, dark and tablet. Put it on your
 *
 * @Composable preview functions instead of repeating three @Preview annotations.
 */
@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Tablet", showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
annotation class DevicePreviews
