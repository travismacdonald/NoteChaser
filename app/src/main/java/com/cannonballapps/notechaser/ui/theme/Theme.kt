// ktlint-disable filename
package com.cannonballapps.notechaser.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = Blue700,
    primaryVariant = Blue800,
    onPrimary = Color.White,
    secondary = Blue700,
    secondaryVariant = Blue800,
    onSecondary = Color.White,
    error = Red800,
)

@Composable
fun NoteChaserTheme(content: @Composable () -> Unit) {
    // todo migrate to M3 theming system
    MaterialTheme(
        content = content,
        colors = LightColors,
        typography = NoteChaserTypography,
        // todo shapes
        // todo dark theme
    )
}
