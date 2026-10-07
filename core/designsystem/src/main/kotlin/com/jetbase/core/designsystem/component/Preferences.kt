package com.jetbase.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/*
 * Reusable kit of "settings rows", inspired by Seal's PreferenceItems.kt.
 * They all share the same anatomy: [icon] title / description [control].
 * They are STATELESS: they receive the value and a callback; they never read preferences
 * themselves (in Seal some components call PreferenceUtil directly).
 */

private val ItemPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

/** Generic clickable row: navigates to another screen or opens a dialog. */
@Composable
fun PreferenceItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    PreferenceRow(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        title = title,
        description = description,
        icon = icon,
        enabled = enabled,
    )
}

/** Row with a switch. The whole row is "toggleable" (accessibility: a single focus). */
@Composable
fun PreferenceSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    PreferenceRow(
        modifier =
            modifier.toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            ),
        title = title,
        description = description,
        icon = icon,
        enabled = enabled,
        trailing = {
            Switch(
                checked = checked,
                onCheckedChange = null, // the whole row handles the click
                enabled = enabled,
                thumbContent =
                    if (checked) {
                        { Icon(Icons.Filled.Check, null, Modifier.size(SwitchDefaults.IconSize)) }
                    } else null,
            )
        },
    )
}

/** One option of a single-choice list (radio button). */
@Composable
fun PreferenceSingleChoiceItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceRow(
        modifier =
            modifier.selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        title = text,
        trailing = { RadioButton(selected = selected, onClick = null) },
    )
}

/** Section header inside a settings list. */
@Composable
fun PreferenceSubtitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun PreferenceRow(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    trailing: (@Composable () -> Unit)? = null,
) {
    val alpha = if (enabled) 1f else 0.38f
    Row(
        modifier = modifier.fillMaxWidth().padding(ItemPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp).size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (!description.isNullOrEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                )
            }
        }
        if (trailing != null) {
            Row(modifier = Modifier.padding(start = 16.dp)) { trailing() }
        }
    }
}

/** Highlighted card (warning, tip). Equivalent to Seal's PreferencesHintCard. */
@Composable
fun PreferenceHintCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(containerColor)
                .clickable(onClick = onClick)
                .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.padding(end = 16.dp),
        )
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, color = contentColor)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = contentColor)
        }
    }
}
