package com.example.movieapp.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MovieDarkColorScheme = darkColorScheme(
    primary = MovieRed,
    primaryContainer = MovieRedDark,
    secondary = ElectricBlue,
    secondaryContainer = DarkCard,
    tertiary = GoldenYellow,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = DarkBackground,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = LightGray,
    outline = DarkGray,
    error = MovieRed
)

@Composable
fun MovieAppTheme(
    darkTheme: Boolean = true, // Always dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = MovieDarkColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}