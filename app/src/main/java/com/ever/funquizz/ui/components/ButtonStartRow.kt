package com.ever.funquizz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.R
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun ButtonStartRow(
    text: String,
    onClick: () -> Unit,
    widthDp: Dp = 245.dp,
    heightDp: Dp = 55.dp,
    clickable: Boolean = true,
    modifier: Modifier = Modifier,
    colors : BoxColors = BoxColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        rippleColor = MaterialTheme.colorScheme.onSecondary
    )
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        BottomEndRoundedButton(
            widthDp = widthDp,
            heightDp = heightDp,
            text = text, onClick = onClick, clickable = clickable, colors = colors)
    }
}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewButtonStartRow() {
    FunQuizzTheme{
        ButtonStartRow(text = "Category", onClick = { /*TODO*/ })
    }
}