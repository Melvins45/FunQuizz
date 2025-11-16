package com.ever.funquizz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.components.BottomRoundedButton
import com.ever.funquizz.ui.theme.FunQuizzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FunQuizzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(
                        start = resources.getString(R.string.start),
                        bestScore = resources.getString(R.string.best_score),
                        parameters = resources.getString(R.string.parameters))
                }
            }
        }
    }
}

@Composable
fun Home(start: String, bestScore: String, parameters:String, modifier: Modifier = Modifier) {

    val spaceBetweenButtons = 25.dp

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(28.dp))
        Image(
            painter = painterResource(id = R.drawable.funquizz_logo),
            contentDescription = "Description de l'image",
            modifier = Modifier
                .height(109.dp)
                .width(286.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        BottomRoundedButton(
            text = "$start",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(spaceBetweenButtons))
        BottomRoundedButton(
            text = "$bestScore",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(spaceBetweenButtons))
        BottomRoundedButton(
            text = "$parameters",
            onClick = { /*TODO*/ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FunQuizzTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Home("t","e", "e")
        }
    }
}