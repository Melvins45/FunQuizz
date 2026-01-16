package com.ever.funquizz.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LetterPerLetterAnim(
    message: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    timeBeforeAppearanceMillis: Int = 0,
    timeToAppearMillis: Int = 150,
    timeBeforeNextAnimationMillis: Int = 1000,
) {
    val lettres = message.toList()
    val visibleStates = remember { lettres.map { mutableStateOf(timeToAppearMillis == 0) } }

    LaunchedEffect(Unit) {
        if (timeToAppearMillis != 0 ) {
            delay(timeBeforeAppearanceMillis.toLong()) // Temps avant de débuter son animation

            while (true) {
                // Réinitialiser
                visibleStates.forEach { it.value = false }

                // Animation lettre par lettre
                for (i in lettres.indices) {
                    visibleStates[i].value = true
                    delay(timeToAppearMillis.toLong()) // Durée d'apparition
                    visibleStates[i].value = false
                    delay(timeToAppearMillis.toLong())
                    //delay(50)  // Petit délai entre lettres
                }

                delay((timeBeforeNextAnimationMillis-timeToAppearMillis*2).toLong()) // Délai avant de recommencer
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        lettres.forEachIndexed { index, char ->
            AnimatedVisibility(
                visible = visibleStates[index].value,
                enter = fadeIn(tween(timeToAppearMillis)),
                exit = fadeOut(tween(timeToAppearMillis))
            ) {
                Text(
                    text = char.toString(),
                    style = textStyle,
                )
            }
        }
    }
}
