package com.fabled.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FabledPrimary = Color(0xFF7C4DFF)
private val FabledSecondary = Color(0xFF03DAC6)
private val FabledBackground = Color(0xFF1A1A2E)
private val FabledSurface = Color(0xFF16213E)
private val FabledOnSurface = Color(0xFFE0E0E0)

private val DarkColorScheme = darkColorScheme(
    primary = FabledPrimary,
    secondary = FabledSecondary,
    background = FabledBackground,
    surface = FabledSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = FabledOnSurface,
    onSurface = FabledOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = FabledPrimary,
    secondary = FabledSecondary,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1A1A2E),
    onSurface = Color(0xFF1A1A2E)
)

@Composable
fun FabledTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
