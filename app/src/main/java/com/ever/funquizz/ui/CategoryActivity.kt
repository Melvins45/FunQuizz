package com.ever.funquizz.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.CategoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.R
import com.ever.funquizz.ui.components.BottomEndRoundedButton
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LogoImage

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
    val categories by viewModel.categories.collectAsState()
    
    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadCategories(categoriesStrings = categoriesStrings)
    })

    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoImage()
        Spacer(modifier = Modifier.height(59.dp))
        /*for(category as categories) {
            Text(text = category.name, style = MaterialTheme.typography.titleMedium)
            category.subcategories.forEach { sub ->
                Text(
                    text = "• ${sub.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }*/
        categories.withIndex().forEach{ (id,category) ->
            if (id%2 == 0){
                ButtonStartRow(text = category.nameCategory, onClick = { /*TODO*/ })
            } else {
                ButtonEndRow(text = category.nameCategory, onClick = { /*TODO*/ })
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FunQuizzTheme {
        CategoryView("Android")
    }
}