@file:Suppress("unused")

package com.stayclean.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimary,
    secondary = AccentGreen,
    onSecondary = OnSecondary,
    background = Color(0xFF121212), // oscuro, coincide con values-night
    surface = Color(0xFF1E1E1E),
    onBackground = OnPrimary,
    onSurface = OnPrimary,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimary,
    secondary = AccentGreen,
    onSecondary = OnSecondary,
    background = Background,
    surface = Surface,
    onBackground = TextHigh,
    onSurface = TextHigh,
    error = Error

    /* Other default colors to override
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun StayCleanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // If dynamicColor is enabled and device supports it, use dynamic schemes from the system.
        // We default to false to ensure our custom brand colors are applied consistently.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
