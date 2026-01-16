package com.ever.funquizz.ui

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
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.CategoryViewModel
import com.ever.funquizz.viewmodel.SettingsViewModel
import com.ever.funquizz.viewmodel.SubCategoryViewModel

class SubCategoryActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(SoundManager)

        val category = intent.getSerializableExtra("Category") as Category

        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SubCategoryView(category, settingsVm =  settingsVm)
                }
            }
        }
    }
}

@Composable
fun SubCategoryView(category: Category, modifier: Modifier = Modifier, viewModel: SubCategoryViewModel = viewModel(), settingsVm: SettingsViewModel? = null) {

    val context = LocalContext.current
    val subCategories by viewModel.subCategories.collectAsState()

    val musicVol by settingsVm?.music?.collectAsState() ?: produceState(0.7f) {  }
    val fxVol by settingsVm?.fx?.collectAsState() ?: produceState(0.7f) {  }

    val nameArray = "sub_category_" + category.nameCategory.lowercase().replace("é","e")
    val resId = context.resources.getIdentifier(nameArray, "array", context.packageName)
    var subCategoriesStrings : List<String> = listOf()
    if (resId != 0) {
        subCategoriesStrings = context.resources.getStringArray(resId).toList()
    }
    val clickFunction : (Int) -> Unit = { index ->
        SoundManager.playSound(context, R.raw.click, fxVol)
        val intent = Intent(context, LevelActivity::class.java)
        intent.putExtra("Category", category)
        intent.putExtra("SubCategory", subCategories[index])
        context.startActivity(intent)
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadSubCategories(category = category, subCategoriesStrings = subCategoriesStrings)
    })

    DisposableEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, musicVol)
        onDispose { SoundManager.stopBackground() }
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
        ButtonStartRow(text = "< " + category.nameCategory, onClick = {
            (context as Activity).finish()
        })
        Spacer(modifier = Modifier.height(40.dp))
        subCategories.withIndex().forEach{ (id,subCategory) ->
            if (id%2 == 0){
                ButtonEndRow(text = subCategory.nameSubCategory, onClick = { clickFunction(id) })
            } else {
                ButtonStartRow(text = subCategory.nameSubCategory, onClick = { clickFunction(id) })
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubCategoryViewPreview() {
    FunQuizzTheme {
        SubCategoryView(Category(20, "Chaud"))
    }
}