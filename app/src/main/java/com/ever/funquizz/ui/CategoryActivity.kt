package com.ever.funquizz.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.CategoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.R

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FunQuizzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CategoryView("Android")
                }
            }
        }
    }
}

@Composable
fun CategoryView(name: String, modifier: Modifier = Modifier, viewModel: CategoryViewModel = viewModel()) {
    val context = LocalContext.current
    val categoriesStrings = context.resources.getStringArray(R.array.category).toList()
    
    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadCategories(categoriesStrings = categoriesStrings)
    })
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FunQuizzTheme {
        CategoryView("Android")
    }
}