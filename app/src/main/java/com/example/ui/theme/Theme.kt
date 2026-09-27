package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TurquoiseDarkColorScheme =
  darkColorScheme(
    primary = TurquoisePrimary,
    onPrimary = TurquoiseOnPrimary,
    primaryContainer = TurquoisePrimaryContainer,
    onPrimaryContainer = TurquoiseOnPrimaryContainer,
    secondary = TurquoiseSecondary,
    onSecondary = TurquoiseOnSecondary,
    secondaryContainer = TurquoiseSecondaryContainer,
    onSecondaryContainer = TurquoiseOnSecondaryContainer,
    tertiary = TurquoiseTertiary,
    onTertiary = TurquoiseOnTertiary,
    tertiaryContainer = TurquoiseTertiaryContainer,
    onTertiaryContainer = TurquoiseOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = TurquoiseDarkColorScheme,
    typography = Typography,
    content = content
  )
}

