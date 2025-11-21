package com.ever.funquizz.model

import androidx.compose.ui.graphics.Color

data class BoxColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color = containerColor.copy(alpha = 0.38f),
    val disabledContentColor: Color = contentColor.copy(alpha = 0.38f),
    val rippleColor: Color = contentColor,
)
