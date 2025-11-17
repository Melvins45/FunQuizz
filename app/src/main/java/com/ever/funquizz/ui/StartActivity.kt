package com.ever.funquizz.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LogoImageClickable
import com.ever.funquizz.ui.components.TextRow
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.LevelViewModel

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getSerializableExtra("Category") as Category
        val subCategory = intent.getSerializableExtra("SubCategory") as SubCategory
        val level = intent.getSerializableExtra("Level") as Level
        
        setContent {
            FunQuizzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StartView(category = category, subCategory = subCategory, level = level)
                }
            }
        }
    }
}

@Composable
fun StartView(category: Category, subCategory: SubCategory, level: Level, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoImageClickable(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(59.dp))
        ButtonStartRow(text = "< " + context.getString(R.string.ready), onClick = {
            (context as Activity).finish()
        })
        TextRow(text = context.getString(R.string.category_label)+" :", arrangementHorizontal = Arrangement.End)
        ButtonEndRow(text = category.nameCategory, onClick = { /*TODO*/ })
        TextRow(text = context.getString(R.string.sub_category_label)+" :", arrangementHorizontal = Arrangement.Start)
        ButtonStartRow(text = subCategory.nameSubCategory, onClick = { /*TODO*/ })
        TextRow(text = context.getString(R.string.difficulty)+" :", arrangementHorizontal = Arrangement.End)
        ButtonEndRow(text = level.nameLevel, onClick = { /*TODO*/ })
        Spacer(modifier = Modifier.height(40.dp))
        ButtonStartRow(
            text = context.getString(R.string.start)+" >",
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.38f),
                disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f)
            )
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    FunQuizzTheme {
        StartView(category = Category(20, "dr"), subCategory = SubCategory(22, 20, "ed"), level = Level(203,"Facile"))
    }
}