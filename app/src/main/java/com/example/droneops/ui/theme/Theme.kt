package com.example.droneops.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkModeFirstColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = Slate950,
    primaryContainer = CyanDark,
    onPrimaryContainer = TextWhite,
    
    secondary = AmberAccent,
    onSecondary = Slate950,
    secondaryContainer = AmberDark,
    onSecondaryContainer = TextWhite,
    
    tertiary = AmberAccent,
    onTertiary = Slate950,
    
    background = Slate950,
    onBackground = TextWhite,
    
    surface = Slate900,
    onSurface = TextWhite,
    
    surfaceVariant = Slate800,
    onSurfaceVariant = TextGray
)

@Composable
fun DroneOpsTheme(
    // Force dark mode first theme, ignoring system settings to maintain outdoor visibility
    darkTheme: Boolean = true,
    // Disable dynamic color so we enforce our high-visibility theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> DarkModeFirstColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
