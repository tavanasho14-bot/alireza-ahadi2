package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceColor,
    primaryContainer = MintContainer,
    onPrimaryContainer = PrimaryGreenDark,
    secondary = PrimaryGreenLight,
    onSecondary = SurfaceColor,
    secondaryContainer = MintLight,
    onSecondaryContainer = PrimaryGreenDark,
    tertiary = AccentGold,
    onTertiary = SurfaceColor,
    tertiaryContainer = AccentGoldLight,
    onTertiaryContainer = AccentGoldDark,
    background = BackgroundColor,
    onBackground = DarkSlate,
    surface = SurfaceColor,
    onSurface = DarkSlate,
    surfaceVariant = MintLight,
    onSurfaceVariant = SlateSecondary,
    outline = BorderColor,
    error = ErrorRed,
    onError = SurfaceColor,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreenLight,
    onPrimary = SurfaceColor,
    primaryContainer = PrimaryGreenDark,
    onPrimaryContainer = MintContainer,
    secondary = MintContainer,
    onSecondary = PrimaryGreenDark,
    background = DarkSlate,
    onBackground = SurfaceColor,
    surface = Color(0xFF1E293B),
    onSurface = SurfaceColor,
    outline = SlateSecondary
)

@Composable
fun SandoghTheme(
    darkTheme: Boolean = false, // Keep clean brand colors matching screenshot
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = PrimaryGreen.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
