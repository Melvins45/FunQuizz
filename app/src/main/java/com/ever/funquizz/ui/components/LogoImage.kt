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
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun LogoImage(
    widthDp: Dp = 286.dp,
    heightDp: Dp = 109.dp,
    isClickable: Boolean = false,
    painterResourceId: Int = R.drawable.funquizz_logo,
    onClick: () -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }

    if (isClickable){
        Image(
            painter = painterResource(id = painterResourceId),
            contentDescription = "Description de l'image",
            modifier = Modifier
                .height(heightDp)
                .width(widthDp)
                .clickable (
                    //interactionSource = interactionSource,
                    //indication = rememberRipple(color = Color.Red),
                    onClick = onClick
                )
        )
    } else {
        Image(
            painter = painterResource(id = painterResourceId),
            contentDescription = "Description de l'image",
            modifier = Modifier
                .height(heightDp)
                .width(widthDp)
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