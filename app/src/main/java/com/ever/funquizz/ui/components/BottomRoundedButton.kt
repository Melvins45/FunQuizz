package com.ever.funquizz.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.FunQuizzTheme



@Composable
fun BottomRoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
    val heightDp = 55.dp

    Button(
        onClick = onClick,
        modifier = modifier
            .height(heightDp)
            .width(210.dp),
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(
            bottomEnd = heightDp,
            bottomStart = heightDp
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text)
    }
}

@Preview(name = "Bouton activé", showBackground = true)
@Composable
fun PreviewAppButtonEnabled() {
    FunQuizzTheme{
        BottomRoundedButton(text = "Valider", onClick = {}, enabled = true)
    }
}

@Preview(name = "Bouton désactivé", showBackground = true)
@Composable
fun PreviewAppButtonDisabled() {
    FunQuizzTheme{
        BottomRoundedButton(text = "Valider", onClick = {}, enabled = false)
    }
}
