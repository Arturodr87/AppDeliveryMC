package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = BucaneroYellow,
    onPrimary = BucaneroBlueDark,
    primaryContainer = BucaneroBlue,
    onPrimaryContainer = BucaneroWhite,
    secondary = BucaneroYellow,
    onSecondary = BucaneroBlueDark,
    tertiary = BucaneroYellowDark,
    background = Color(0xFF0D1B2A),
    surface = Color(0xFF1B2A3D),
    onBackground = BucaneroWhite,
    onSurface = BucaneroWhite,
    surfaceVariant = Color(0xFF243B53),
    outline = BorderMediumGray
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BucaneroBlue,
    onPrimary = BucaneroWhite,
    primaryContainer = BucaneroBlueContainer,
    onPrimaryContainer = BucaneroOnBlueContainer,
    secondary = BucaneroYellow,
    onSecondary = BucaneroOnYellowContainer,
    secondaryContainer = BucaneroYellowContainer,
    onSecondaryContainer = BucaneroOnYellowContainer,
    tertiary = BucaneroBlueLight,
    background = SurfaceBackground,
    surface = SurfaceCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = BorderSoftGray,
    outlineVariant = BorderMediumGray
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep consistent corporate identity by default
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
