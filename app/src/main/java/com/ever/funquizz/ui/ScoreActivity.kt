package com.ever.funquizz.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LetterPerLetterAnim
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.components.RoundedColumn
import com.ever.funquizz.ui.components.RoundedRow
import com.ever.funquizz.ui.components.TextBox
import com.ever.funquizz.ui.components.TopRoundedButton
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.CategoryViewModel
import kotlinx.coroutines.delay

class ScoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getSerializableExtra("Category") as Category
        val subCategory = intent.getSerializableExtra("SubCategory") as SubCategory
        val level = intent.getSerializableExtra("Level") as Level
        val score = intent.getIntExtra("Score", 10)

        setContent {
            FunQuizzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScoreView(category, subCategory, level, score)
                }
            }
        }
    }
}


@Composable
fun ScoreView(category: Category, subCategory: SubCategory, level: Level, score: Int, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var isWaiting by remember { mutableStateOf(true) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    var backgroundStateColor by remember { mutableStateOf(primaryColor) }
    var filterStateColor by remember { mutableStateOf(primaryColor) }
    var titleStateColor by remember { mutableStateOf(Color.Transparent) }

    var backgroundRow1StateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow1StateColor by remember { mutableStateOf(Color.Transparent) }
    var backgroundRow1BoxStateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow1BoxStateColor by remember { mutableStateOf(Color.Transparent) }

    var backgroundRow2StateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow2StateColor by remember { mutableStateOf(Color.Transparent) }
    var backgroundRow2BoxStateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow2BoxStateColor by remember { mutableStateOf(Color.Transparent) }

    var backgroundRow3StateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow3StateColor by remember { mutableStateOf(Color.Transparent) }
    var backgroundRow3BoxStateColor by remember { mutableStateOf(Color.Transparent) }
    var onBackgroundRow3BoxStateColor by remember { mutableStateOf(Color.Transparent) }

    val timeToAppearMillis = 300
    val timeBeforeAppearanceMillis: (Int) -> Int = {
        timeToAppearMillis * (it%5)
    }
    val timeBeforeNextAnimationMillis = timeToAppearMillis * 5

    val backgroundAnimateColor by animateColorAsState(
        targetValue = backgroundStateColor,
        animationSpec = tween(durationMillis = 2500),
        finishedListener = {
            isWaiting = false
        }
    )

    val filterAnimateColor by animateColorAsState(
        targetValue = filterStateColor,
        animationSpec = tween(durationMillis = 2500)
    )

    val titleAnimateColor by animateColorAsState(
        targetValue = titleStateColor,
        animationSpec = tween(durationMillis = 1500)
    )

    val backgroundRow2AnimateColor by animateColorAsState(
        targetValue = backgroundRow2StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow2AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow2StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundRow2BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow2BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow2BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow2BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundRow3AnimateColor by animateColorAsState(
        targetValue = backgroundRow3StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow3AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow3StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundRow3BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow3BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow3BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow3BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundRow1AnimateColor by animateColorAsState(
        targetValue = backgroundRow1StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow1AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow1StateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundRow1BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow1BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val onBackgroundRow1BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow1BoxStateColor,
        animationSpec = tween(durationMillis = 500)
    )

    val clickFunction : (Int) -> Unit = { index ->
        val intent = Intent(context, SubCategoryActivity::class.java)
        //intent.putExtra("Category", categories[index])
        context.startActivity(intent)
    }

    LaunchedEffect(key1 = Unit, block = {
        //viewModel.loadCategories(categoriesStrings = categoriesStrings)
        delay(2600)
        backgroundStateColor = backgroundColor
        filterStateColor = onPrimaryColor
        delay(1000)
        titleStateColor = onPrimaryColor
        delay(500)
        backgroundRow1StateColor = surfaceColor
        onBackgroundRow1StateColor = onSurfaceColor
        backgroundRow1BoxStateColor = primaryColor
        onBackgroundRow1BoxStateColor = onPrimaryColor
        delay(500)
        backgroundRow2StateColor = surfaceColor
        onBackgroundRow2StateColor = onSurfaceColor
        backgroundRow2BoxStateColor = primaryColor
        onBackgroundRow2BoxStateColor = onPrimaryColor
        delay(500)
        backgroundRow3StateColor = surfaceColor
        onBackgroundRow3StateColor = onSurfaceColor
        backgroundRow3BoxStateColor = primaryColor
        onBackgroundRow3BoxStateColor = onPrimaryColor
    })

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(backgroundAnimateColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Logo
        //if (!isWaiting) {
            LogoImage(
                heightDp = 36.dp,
                widthDp = 90.dp,
                paddingValues = PaddingValues(top = 18.dp, bottom = 12.dp),
                isClickable = true,
                painterResourceId = R.drawable.funquizz_mini_logo,
                colorFilter = filterAnimateColor,
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(intent)
                }
            )
        /*} else {
            Spacer(modifier = Modifier.height(66.dp))
        }*/

        /*TopRoundedButton(
            text = "\\  |  /",
            enabled = false,
            heightDp = 50.dp,
            widthDp = 210.dp,
            onClick = { /*TODO*/ },
            colors = BoxColors(
                containerColor = Color.Black,
                contentColor = Color.Black,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )*/

        // First Row For animation
        RoundedRow(
            cornerShape = RoundedCornerShape(
                topStart = 50.dp,
                topEnd = 50.dp
            ),
            heightDp = 50.dp,
            widthDp = 210.dp,
            colors = BoxColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            LetterPerLetterAnim(
                message = "\\",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(0),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "\\",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(1),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "|",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(2),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "/",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(3),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "/",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(4),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
        }
        Row (
            //modifier = modifier.height(210.dp),
            horizontalArrangement = Arrangement.Center
                ) {
            // Row for animate on left
            RoundedColumn(
                cornerShape = RoundedCornerShape(
                    topStart = 210.dp,
                    bottomStart = 210.dp
                ),
                angleRotate = -180f,
                heightDp = 210.dp,
                widthDp = 50.dp,
                colors = BoxColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                LetterPerLetterAnim(
                    message = "\\",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(15),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "\\",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(16),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "|",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(17),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "/",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(18),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "/",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(19),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
            }

            // Score in middle
            TextBox(
                text = score.toString(),
                modifier = modifier.width(210.dp),
                heightDp = 210.dp,
                textStyle = MaterialTheme.typography.titleLarge
            )

            // Row for animate on right
            RoundedColumn(
                cornerShape = RoundedCornerShape(
                    topEnd = 210.dp,
                    bottomEnd = 210.dp
                ),
                //angleRotate = 90f,
                heightDp = 210.dp,
                widthDp = 50.dp,
                colors = BoxColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                LetterPerLetterAnim(
                    message = "\\",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(10),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "\\",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(11),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "|",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(12),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "/",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(13),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
                LetterPerLetterAnim(
                    message = "/",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .rotate(90f),
                    timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(14),
                    timeToAppearMillis = timeToAppearMillis,
                    timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
                )
            }
        }
        RoundedRow(
            cornerShape = RoundedCornerShape(
                bottomStart = 50.dp,
                bottomEnd = 50.dp
            ),
            angleRotate = -180f,
            heightDp = 50.dp,
            widthDp = 210.dp,
            colors = BoxColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            LetterPerLetterAnim(
                message = "\\",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(5),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "\\",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(6),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "|",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(7),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "/",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(8),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
            LetterPerLetterAnim(
                message = "/",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                timeBeforeAppearanceMillis = timeBeforeAppearanceMillis(9),
                timeToAppearMillis = timeToAppearMillis,
                timeBeforeNextAnimationMillis = timeBeforeNextAnimationMillis
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Title
        Text(
            text = context.resources.getStringArray(R.array.score_titles)[score],
            style = MaterialTheme.typography.titleMedium,
            color = titleAnimateColor
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Row for Category
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundRow1AnimateColor)
                ) {
            TextBox(
                text = context.getString(R.string.category_label),
                modifier = modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(topEnd = 50.dp)
                    ),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = backgroundRow1BoxAnimateColor,
                    contentColor = onBackgroundRow1BoxAnimateColor
                )
            )
            TextBox(
                text = category.nameCategory,
                modifier = modifier
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = Color.Transparent,
                    contentColor = onBackgroundRow1AnimateColor
                )
            )
        }

        // Row for SubCategory
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundRow2AnimateColor)
        ) {
            TextBox(
                text = context.getString(R.string.sub_category_label),
                modifier = modifier
                    .fillMaxHeight()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = backgroundRow2BoxAnimateColor,
                    contentColor = onBackgroundRow2BoxAnimateColor
                )
            )
            TextBox(
                text = subCategory.nameSubCategory,
                modifier = modifier
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = Color.Transparent,
                    contentColor = onBackgroundRow2AnimateColor
                )
            )
        }

        // Row for Level
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundRow3AnimateColor)
        ) {
            TextBox(
                text = context.getString(R.string.difficulty),
                modifier = modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(bottomEnd = 50.dp)
                    ),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = backgroundRow3BoxAnimateColor,
                    contentColor = onBackgroundRow3BoxAnimateColor
                )
            )
            TextBox(
                text = level.nameLevel,
                modifier = modifier
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = BoxColors(
                    containerColor = Color.Transparent,
                    contentColor = onBackgroundRow3AnimateColor
                )
            )
        }
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
        /*categories.withIndex().forEach{ (id,category) ->
            if (id%2 == 0){
                ButtonStartRow(text = category.nameCategory, onClick = { clickFunction(id) })
            } else {
                ButtonEndRow(text = category.nameCategory, onClick = { clickFunction(id) })
            }
            Spacer(modifier = Modifier.height(40.dp))
        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    FunQuizzTheme {
        ScoreView(category = Category(20, "dr"), subCategory = SubCategory(22, 20, "ed"), level = Level(203,"Facile"), score = 10)
    }
}