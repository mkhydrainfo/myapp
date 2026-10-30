package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Emerald500,
    onPrimary = Color.White,
    primaryContainer = Emerald800,
    onPrimaryContainer = Emerald100,
    secondary = Emerald500,
    onSecondary = Color.White,
    background = Neutral900,
    onBackground = Color(0xFFF1F5F9),
    surface = Neutral800,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Neutral700,
    onSurfaceVariant = Neutral300,
    outline = Neutral600
)

private val LightColorScheme = lightColorScheme(
    primary = Emerald600,
    onPrimary = Color.White,
    primaryContainer = Emerald100,
    onPrimaryContainer = Emerald900,
    secondary = Emerald700,
    onSecondary = Color.White,
    background = Neutral50,
    onBackground = Neutral900,
    surface = Color.White,
    onSurface = Neutral900,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral600,
    outline = Neutral200
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep false to maintain MoneyFlow's distinct emerald brand
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
