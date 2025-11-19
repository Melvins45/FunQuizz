package com.ever.funquizz.model

import androidx.compose.ui.graphics.Color

data class BoxColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color = containerColor.copy(alpha = 0.48f),
    val disabledContentColor: Color = contentColor.copy(alpha = 0.48f),
    val rippleColor: Color = contentColor,
)
