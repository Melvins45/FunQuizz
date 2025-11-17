package com.ever.funquizz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun BottomEndRoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    widthDp: Dp = 245.dp,
    heightDp: Dp = 55.dp,
    enabled: Boolean = true,
    isButton: Boolean = true,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
    )
) {

    val lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
    val radiusDp = with(LocalDensity.current) { lineHeight.toDp() }

    if(isButton) {
        Button(
            onClick = onClick,
            modifier = modifier
                .height(heightDp)
                .width(widthDp)
            ,
            enabled = enabled,
            colors = colors,
            shape = RoundedCornerShape(
                bottomEnd = heightDp
            )
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    } else {
        Box(
            modifier = modifier
                .height(heightDp)
                .width(widthDp)
                .clip(
                    RoundedCornerShape(
                        bottomEnd = heightDp
                    )
                )
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                modifier = modifier
                    .height(heightDp)
                    .width(widthDp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewAppButton2Enabled() {
    FunQuizzTheme{
        BottomEndRoundedButton(text = "Valider", onClick = {}, enabled = true)
    }
}

@Preview(name = "Bouton désactivé", showBackground = true)
@Composable
fun PreviewAppButton2Disabled() {
    FunQuizzTheme{
        BottomEndRoundedButton(text = "Valider", onClick = {}, enabled = false)
    }
}
