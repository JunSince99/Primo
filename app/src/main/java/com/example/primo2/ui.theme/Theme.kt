package com.example.primo2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.White, // 내용물
    primaryVariant = Color.White,
    secondary = DarkRed, // ??
    onPrimary = DarkRed,
    error = DarkRed,
    onBackground = Color.Black //배경색
)

private val LightColorPalette = lightColors(
    primary = Color.Black, // 내용물
    primaryVariant = Color.Black,
    secondary = Color.White,
    onPrimary = Color.White,
    error = DarkRed,
    onBackground = Color.White // 배경


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun LazyColumnExampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}