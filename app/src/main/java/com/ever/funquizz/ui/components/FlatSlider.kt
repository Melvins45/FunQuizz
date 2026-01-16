package com.ever.funquizz.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.primaryDisabled



@Composable
fun FlatSlider(
    value: Float,
    onValue: (Float) -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Float = 1f,
    widthDp: Dp? = null,
    heightDp: Dp = 24.dp,
    thumbRadius: Dp = heightDp / 2,
    shadowRadius: Dp = thumbRadius * 1.125f,
    activeTrackColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.primaryDisabled,
    thumbColor: Color = activeTrackColor,
    shadowColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    val sizeMod = modifier.then(
        when (widthDp) {
            null -> Modifier.fillMaxWidth().height(heightDp)
            else -> Modifier.width(widthDp).height(heightDp)
        }
    )

    BoxWithConstraints(sizeMod) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val thumbPx = with(density) { thumbRadius.toPx() }
        val shadowPx = with(density) { shadowRadius.toPx() }

        /* état animé */
        var position by remember { mutableStateOf(value / maxValue) }
        LaunchedEffect(value, maxValue) {
            position = (value / maxValue).coerceIn(0f, 1f)
        }


        val offset = remember(position) {
            (maxWidth - thumbRadius * 2) * position
        }

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offsetStart ->
                            val pct = (offsetStart.x / widthPx).coerceIn(0f, 1f)
                            position = pct
                            onValue(pct * maxValue)
                        },
                        onDrag = { change, _ ->
                            val pct = (change.position.x / widthPx).coerceIn(0f, 1f)
                            position = pct
                            onValue(pct * maxValue)
                            change.consume()
                        }
                    )
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val centerY = size.height / 2f

                /* track non immersé */
                drawRoundRect(
                    color = inactiveTrackColor,
                    topLeft = Offset(0f, centerY - size.height / 4),
                    size = Size(size.width, size.height / 2),
                    cornerRadius = CornerRadius(size.height / 2)
                )

                /* track immersé */
                drawRoundRect(
                    color = activeTrackColor,
                    topLeft = Offset(0f, centerY - size.height / 4),
                    size = Size(offset.toPx() + thumbPx, size.height / 2),
                    cornerRadius = CornerRadius(size.height / 2)
                )

                /* ombre */
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        color = thumbColor.toArgb()
                        setShadowLayer(
                            shadowPx, 0f, 2.dp.toPx(), shadowColor.toArgb()
                        )
                    }
                    canvas.nativeCanvas.drawCircle(
                        offset.toPx() + thumbPx,
                        centerY,
                        shadowPx,
                        paint
                    )
                }

                /* thumb */
                drawCircle(
                    color = thumbColor,
                    radius = thumbPx,
                    center = Offset(offset.toPx() + thumbPx, centerY)
                )
            }
        }
    }
}