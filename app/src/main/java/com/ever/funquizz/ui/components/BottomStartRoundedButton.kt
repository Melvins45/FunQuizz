package com.ever.funquizz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun BottomStartRoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    widthDp: Dp = 245.dp,
    heightDp: Dp = 55.dp,
    clickable: Boolean = true,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    colors : BoxColors = BoxColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        rippleColor = MaterialTheme.colorScheme.onSecondary
    )
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
        targetValue = if (enabled) colors.containerColor else colors.disabledContainerColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundColor by animateColorAsState(
        targetValue = if (enabled) colors.contentColor else colors.disabledContentColor,
        animationSpec = tween(durationMillis = 500)
    )

    if(enabled) {
        Box(
            modifier = modifier
                .height(heightDp)
                .width(widthDp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = heightDp,
                    )
                )
                .then(
                    if (clickable) modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(color = colors.rippleColor, bounded = true),
                        onClick = onClick
                    ) else modifier
                )
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = textStyle,
                color = onBackgroundColor
            )
        }
    } else {
        Box(
            modifier = modifier
                .height(heightDp)
                .width(widthDp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = heightDp,
                    )
                )
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = textStyle,
                color = onBackgroundColor
            )
        }
    }
}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewAppButton3Enabled() {
    FunQuizzTheme{
        BottomStartRoundedButton(text = "Valider", onClick = {}, enabled = true)
    }
}

@Preview(name = "Bouton désactivé", showBackground = true)
@Composable
fun PreviewAppButton3Disabled() {
    FunQuizzTheme{
        BottomStartRoundedButton(text = "Valider", onClick = {}, enabled = false)
    }
}
