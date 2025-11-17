package com.ever.funquizz.model

import androidx.compose.ui.graphics.Color

data class BoxColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val rippleColor: Color = contentColor,
)
