package com.jetbase.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

/**
 * Color swatch to pick the seed color. It paints the primary/secondary/tertiary tones that seed
 * WOULD generate, like Seal's ColorButton.
 */
@Composable
fun ColorSwatch(
    seedColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    style: PaletteStyle = PaletteStyle.TonalSpot,
) {
    val scheme =
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = false,
            style = style,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
        )
    val checkSize by animateDpAsState(if (selected) 28.dp else 0.dp, label = "checkSize")

    Box(
        modifier =
            modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
                .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(scheme.primaryContainer)) {
            Box(
                Modifier.size(22.dp)
                    .align(Alignment.BottomStart)
                    .background(scheme.secondaryContainer)
            )
            Box(Modifier.size(22.dp).align(Alignment.BottomEnd).background(scheme.tertiary))
        }
        Box(
            Modifier.size(checkSize).clip(CircleShape).background(scheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = scheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
