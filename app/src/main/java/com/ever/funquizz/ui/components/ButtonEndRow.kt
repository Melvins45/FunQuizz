package com.ever.funquizz.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun ButtonEndRow(
    text: String,
    onClick: () -> Unit,
    widthDp: Dp = 286.dp,
    heightDp: Dp = 109.dp,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
    )
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        BottomStartRoundedButton(text = text, onClick = onClick, colors = colors)
    }
}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewButtonEndRow() {
    FunQuizzTheme{
        ButtonEndRow(text = "Category", onClick = { /*TODO*/ })
    }
}