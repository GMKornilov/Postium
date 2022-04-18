package com.gmkornilov.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color.Black.copy(alpha = 0.12f),
)

private val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = LightGreen700,
    secondary = Indigo600,
    secondaryVariant = Indigo400,
    background = Indigo50,

    onPrimary = Color.White,
    onBackground = Color.Black,
    onSecondary = Color.White,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val LocalSystemUiController = staticCompositionLocalOf<SystemUiController> {
    error("Default value not provided")
}

@Composable
fun PostiumTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}