package com.ever.funquizz.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.ever.funquizz.model.Theme

/* Renvoie true -> thème sombre */
@Composable
fun resolveDarkTheme(theme: Theme): Boolean = when (theme) {
    Theme.SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT  -> false
    Theme.DARK   -> true
}