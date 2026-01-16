package com.ever.funquizz.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.factory.SettingsViewModelFactory
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.SoundManager
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.LevelViewModel
import com.ever.funquizz.viewmodel.SettingsViewModel
import com.ever.funquizz.viewmodel.SubCategoryViewModel

class LevelActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val category = intent.getSerializableExtra("Category") as Category
        val subCategory = intent.getSerializableExtra("SubCategory") as SubCategory
        
        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LevelView(category = category, subCategory = subCategory, settingsVm =  settingsVm)
                }
            }
        }
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun LevelView(category: Category, subCategory: SubCategory, modifier: Modifier = Modifier, viewModel: LevelViewModel = viewModel(), settingsVm: SettingsViewModel? = null) {

    val context = LocalContext.current
    val levels by viewModel.levels.collectAsState()

    val musicVol by settingsVm?.music?.collectAsState() ?: produceState(0.7f) {  }
    val fxVol by settingsVm?.fx?.collectAsState() ?: produceState(0.7f) {  }

    val levelsStrings = context.resources.getStringArray(R.array.level).toList()
    val clickFunction : (Int) -> Unit = { index ->
        SoundManager.playSound(context, R.raw.click, fxVol)
        val intent = Intent(context, StartActivity::class.java)
        intent.putExtra("Category", category)
        intent.putExtra("SubCategory", subCategory)
        intent.putExtra("Level", levels[index])
        context.startActivity(intent)
    }


    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadLevels(levelsStrings = levelsStrings)
    })

    DisposableEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, musicVol)
        onDispose { /*SoundManager.stopBackground()*/ }
    }

    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoImage(
            isClickable = true,
            onClick = {
                SoundManager.playSound(context, R.raw.click, fxVol)
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(59.dp))
        ButtonStartRow(text = "< " + context.getString(R.string.difficulty), onClick = {
            SoundManager.playSound(context, R.raw.click, fxVol)
            (context as Activity).finish()
        })
        Spacer(modifier = Modifier.height(40.dp))
        levels.withIndex().forEach{ (id,level) ->
            if (id%2 == 0){
                ButtonEndRow(text = level.nameLevel, onClick = { clickFunction(id) })
            } else {
                ButtonStartRow(text = level.nameLevel, onClick = { clickFunction(id) })
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    FunQuizzTheme {
        LevelView(category = Category(20, "chaud"), subCategory = SubCategory(22,20,"chau"))
    }
}