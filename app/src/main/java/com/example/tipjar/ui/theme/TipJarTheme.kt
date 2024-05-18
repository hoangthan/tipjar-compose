package com.example.tipjar.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TipJarTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = ColorPalette, typography = TipJarTypography, content = content)
}
