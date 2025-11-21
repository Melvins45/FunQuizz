package com.ever.funquizz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.R
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun LogoImage(
    modifier: Modifier = Modifier,
    widthDp: Dp = 286.dp,
    heightDp: Dp = 109.dp,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    isClickable: Boolean = false,
    painterResourceId: Int = R.drawable.funquizz_logo,
    onClick: () -> Unit = {},
    colorFilter: Color = MaterialTheme.colorScheme.onPrimary,
    animationDurationMillis: Int = 0
) {

    val interactionSource = remember { MutableInteractionSource() }


    val colorAnimateFilter by animateColorAsState(
        targetValue = colorFilter,
        animationSpec = tween(durationMillis = animationDurationMillis)
    )

    if (isClickable){
        Image(
            painter = painterResource(id = painterResourceId),
            contentDescription = "Description de l'image",
            modifier = modifier
                .padding(paddingValues)
                .height(heightDp)
                .width(widthDp)
                .clickable (
                    //interactionSource = interactionSource,
                    //indication = rememberRipple(color = Color.Red),
                    onClick = onClick
                ),
            colorFilter = ColorFilter.tint(colorFilter)
        )
    } else {
        Image(
            painter = painterResource(id = painterResourceId),
            contentDescription = "Description de l'image",
            modifier = modifier
                .padding(paddingValues)
                .height(heightDp)
                .width(widthDp),
            colorFilter = ColorFilter.tint(colorFilter)
        )
    }

}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewLogoImage() {
    FunQuizzTheme{
        LogoImage()
    }
}