package com.ever.funquizz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ever.funquizz.factory.SettingsViewModelFactory
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Party
import com.ever.funquizz.model.PartyRepository
import com.ever.funquizz.model.SoundManager
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.BestScoreActivity
import com.ever.funquizz.ui.CategoryActivity
import com.ever.funquizz.ui.ParametersActivity
import com.ever.funquizz.ui.ScoreActivity
import com.ever.funquizz.ui.components.BottomRoundedButton
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.SettingsViewModel
import java.util.Date
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(SoundManager)

        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(
                        start = resources.getString(R.string.start),
                        bestScore = resources.getString(R.string.best_score),
                        parameters = resources.getString(R.string.parameters),
                        settingsVm =  settingsVm)
                }
            }
        }
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun Home(start: String, bestScore: String, parameters:String, modifier: Modifier = Modifier, settingsVm: SettingsViewModel? = null) {

    val context = LocalContext.current
    val spaceBetweenButtons = 25.dp

    val musicVol by settingsVm?.music?.collectAsState() ?: produceState(0.7f) {  }
    val fxVol by settingsVm?.fx?.collectAsState() ?: produceState(0.7f) {  }

    /*LaunchedEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, musicVol)
    }*/

    DisposableEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, musicVol)
        onDispose { SoundManager.stopBackground() }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(28.dp))
        LogoImage()
        Spacer(modifier = Modifier.height(35.dp))
        BottomRoundedButton(
            text = "$start",
            onClick = {
                SoundManager.playSound(context, R.raw.click, fxVol)
                val intent = Intent(context, CategoryActivity::class.java)
                /*intent.putExtra("Category", Category(20, "Pays"))
                intent.putExtra("SubCategory", SubCategory(22, 20, "Présidents"))
                intent.putExtra("Level", Level(203,"Facile"))*/
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(spaceBetweenButtons))
        BottomRoundedButton(
            text = "$bestScore",
            onClick = {
                /*val repo = PartyRepository(context)
                val party = Party(
                    idParty = repo.nextId(),
                    category = Category(20, "Pays"),
                    subCategory = SubCategory(22, 20, "Présidents"),
                    level = Level(203,listOf("Facile", "Moyen", "Difficile").random()),
                    score = (1..10).random(),
                    date = Date()
                )
                repo.saveParty(party)*/
                SoundManager.playSound(context, R.raw.click, fxVol)
                val intent = Intent(context, BestScoreActivity::class.java)
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(spaceBetweenButtons))
        BottomRoundedButton(
            text = "$parameters",
            onClick = {
                SoundManager.playSound(context, R.raw.click, fxVol)
                val intent = Intent(context, ParametersActivity::class.java)
                context.startActivity(intent)
            }
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