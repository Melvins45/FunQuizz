package com.ever.funquizz.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.ui.theme.FunQuizzTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextBox(
    text: String,
    //widthDp: Dp = 250.dp,
    heightDp: Dp = 40.dp,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    colors: BoxColors = BoxColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
    )
) {

    Box(
        modifier = modifier
            .height(heightDp)
            //.fillMaxWidth()
            .background(colors.containerColor),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                fadeIn(tween(500)) with fadeOut(tween(500))
            }
        ) { message ->
            Text(
                text = message,
                modifier = Modifier
                    .padding(paddingValues),
                textAlign = TextAlign.Center,
                style = textStyle,
                color = colors.contentColor
            )
        }
    }
}

@Preview(name = "Text In Box", showBackground = true)
@Composable
fun PreviewTextBox() {
    FunQuizzTheme{
        TextBox(text = "Category")
    }
}