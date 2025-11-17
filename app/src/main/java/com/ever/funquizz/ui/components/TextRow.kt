package com.ever.funquizz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.FunQuizzTheme

@Composable
fun TextRow(
    text: String,
    widthDp: Dp = 250.dp,
    heightDp: Dp = 40.dp,
    arrangementHorizontal: Arrangement.Horizontal = Arrangement.Center,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp),
        horizontalArrangement = arrangementHorizontal,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = modifier
                .width(widthDp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "Text In Row", showBackground = true)
@Composable
fun PreviewTextRow() {
    FunQuizzTheme{
        TextRow(text = "Category")
    }
}