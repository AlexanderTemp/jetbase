package com.jetbase.core.ui.theme

import com.jetbase.core.model.ThemeStyle
import com.materialkolor.PaletteStyle

/**
 * Bridge between the saved preference (core:model) and the color library (core:designsystem). It
 * lives in core:ui because that is the only module that knows both.
 */
fun ThemeStyle.toPaletteStyle(): PaletteStyle =
    when (this) {
        ThemeStyle.TonalSpot -> PaletteStyle.TonalSpot
        ThemeStyle.Neutral -> PaletteStyle.Neutral
        ThemeStyle.Vibrant -> PaletteStyle.Vibrant
        ThemeStyle.Expressive -> PaletteStyle.Expressive
        ThemeStyle.Rainbow -> PaletteStyle.Rainbow
        ThemeStyle.FruitSalad -> PaletteStyle.FruitSalad
        ThemeStyle.Monochrome -> PaletteStyle.Monochrome
    }
