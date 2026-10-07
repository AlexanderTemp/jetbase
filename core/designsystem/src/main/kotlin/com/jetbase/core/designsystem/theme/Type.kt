package com.jetbase.core.designsystem.theme

import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDirection

/**
 * Default Material 3 typography with two tweaks Seal applies to every style:
 * - LineBreak.Paragraph: breaks lines in a more balanced way in long texts.
 * - TextDirection.Content: the direction (LTR/RTL) is decided by the CONTENT, not the system
 *   locale.
 */
val JetBaseTypography: Typography =
    Typography().run {
        copy(
            displayLarge = displayLarge.adjusted(),
            displayMedium = displayMedium.adjusted(),
            displaySmall = displaySmall.adjusted(),
            headlineLarge = headlineLarge.adjusted(),
            headlineMedium = headlineMedium.adjusted(),
            headlineSmall = headlineSmall.adjusted(),
            titleLarge = titleLarge.adjusted(),
            titleMedium = titleMedium.adjusted(),
            titleSmall = titleSmall.adjusted(),
            bodyLarge = bodyLarge.adjusted(paragraph = true),
            bodyMedium = bodyMedium.adjusted(paragraph = true),
            bodySmall = bodySmall.adjusted(paragraph = true),
            labelLarge = labelLarge.adjusted(),
            labelMedium = labelMedium.adjusted(),
            labelSmall = labelSmall.adjusted(),
        )
    }

val JetBaseShapes: Shapes = Shapes()

private fun TextStyle.adjusted(paragraph: Boolean = false): TextStyle =
    copy(
        textDirection = TextDirection.Content,
        lineBreak = if (paragraph) LineBreak.Paragraph else lineBreak,
    )
