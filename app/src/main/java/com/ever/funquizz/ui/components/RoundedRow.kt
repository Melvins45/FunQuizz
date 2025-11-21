package com.ever.funquizz.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.ui.theme.FunQuizzTheme



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RoundedRow(
    cornerShape: RoundedCornerShape = RoundedCornerShape(55.dp),
    modifier: Modifier = Modifier,
    widthDp: Dp = 212.dp,
    heightDp: Dp = 55.dp,
    angleRotate : Float = 0f,
    colors : BoxColors = BoxColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        rippleColor = MaterialTheme.colorScheme.onSecondary
    ),
    content: @Composable (RowScope.() -> Unit)
    /*colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
    )*/
) {

    val lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
    val radiusDp = with(LocalDensity.current) { lineHeight.toDp() }

    val interactionSource = remember { MutableInteractionSource() }

    val backgroundColor by animateColorAsState(
        targetValue = colors.containerColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundColor by animateColorAsState(
        targetValue = colors.contentColor,
        animationSpec = tween(durationMillis = 500)
    )

    Row(
        modifier = modifier
            .height(heightDp)
            .width(widthDp)
            .clip(
                cornerShape
            )
            .rotate(angleRotate)
            .background(backgroundColor),
        //contentAlignment = Alignment.Center,
        content = content
    )
}

@Preview(name = "Rounded Row", showBackground = true)
@Composable
fun PreviewTopRoundedBoxEnabled() {
    FunQuizzTheme{
        RoundedRow() {}
    }
}
