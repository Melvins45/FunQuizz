package com.ever.funquizz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.primaryDisabled

@Composable
fun OnOffSwitch(
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    widthDp: Dp = 52.dp,
    heightDp: Dp = 28.dp,
    bgOn: Color = MaterialTheme.colorScheme.primary,
    bgOff: Color = MaterialTheme.colorScheme.primaryDisabled,
    thumbOn: Color = MaterialTheme.colorScheme.primaryDisabled,
    thumbOff: Color = MaterialTheme.colorScheme.primary,
    iconOn: ImageVector = Icons.Rounded.LightMode,
    iconOff: ImageVector = Icons.Rounded.DarkMode,
    shadowColor: Color = Color(0x40000000)
) {
    val density = LocalDensity.current
    val maxOffset = widthDp - heightDp

    /* animations de couleur */
    val bg by animateColorAsState(if (checked) bgOn else bgOff, label = "bg")
    val thumb by animateColorAsState(if (checked) thumbOn else thumbOff, label = "thumb")

    /* position animée */
    val offset by animateDpAsState(
        if (checked) maxOffset else 0.dp,
        animationSpec = tween(200), label = "offset"
    )

    Box(
        modifier
            .width(widthDp)
            .height(heightDp)
            .clip(RoundedCornerShape(heightDp / 2))
            .background(bg)
            .clickable { onChecked(!checked) }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        /* icône + ombre */
        Icon(
            imageVector = if (checked) iconOn else iconOff,
            contentDescription = null,
            tint = thumb,
            modifier = Modifier
                .size(heightDp - 8.dp)
                .offset(x = offset)
                .drawWithContent {
                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            color = thumb.toArgb()
                            setShadowLayer(
                                8.dp.toPx(), 0f, 2.dp.toPx(), shadowColor.toArgb()
                            )
                        }
                        canvas.nativeCanvas.drawCircle(
                            size.width / 2f,
                            size.height / 2f,
                            size.minDimension / 2f,
                            paint
                        )
                    }
                    drawContent()
                }
        )
    }
}