package com.ever.funquizz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

val lightPrimaryDisabled = Color(0xFF7577FE)
val lightSecondaryDisabled = Color(0xFF9BFF96)
val lightOnPrimaryDisabled = Color(0xFFD9D9D9)
val lightOnSecondaryDisabled = Color(0xFF424242)
val darkPrimaryDisabled = Color(0xFF4E5780)
val darkSecondaryDisabled = Color(0xFF649761)
val darkOnPrimaryDisabled = Color(0xFF939292)
val darkOnSecondaryDisabled = Color(0xFFD9D9D9)

val ColorScheme.primaryDisabled: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkPrimaryDisabled else lightPrimaryDisabled

val ColorScheme.onPrimaryDisabled: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkOnPrimaryDisabled else lightOnPrimaryDisabled

val ColorScheme.secondaryDisabled: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkSecondaryDisabled else lightSecondaryDisabled

val ColorScheme.onSecondaryDisabled: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkOnSecondaryDisabled else lightOnSecondaryDisabled

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00157C),
    secondary = Color(0xFF08A700),
    tertiary = Color(0xFFA10000),
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF1C1C1C),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFEFEFE),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFEFEFE),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0004FF),
    secondary = Color(0xFF0DFD00),
    tertiary = Color(0xFFFD1313),
    background = Color(0xFFF21EE3),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
)

@Composable
fun FunQuizzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}