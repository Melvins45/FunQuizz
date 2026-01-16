package com.ever.funquizz.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.factory.SettingsViewModelFactory
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.components.LetterPerLetterAnim
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.components.RoundedColumn
import com.ever.funquizz.ui.components.RoundedRow
import com.ever.funquizz.ui.components.TextBox
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.SettingsViewModel
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.android.awaitFrame

class ScreenShotActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getSerializableExtra("Category") as Category
        val subCategory = intent.getSerializableExtra("SubCategory") as SubCategory
        val level = intent.getSerializableExtra("Level") as Level
        val score = intent.getIntExtra("Score", 10)
        val isSharing = intent.getBooleanExtra("isSharing", false)

        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                val screenshotState = rememberScreenshotState()
                var ready by remember { mutableStateOf(false) }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenShotView(screenshotState, category, subCategory, level, score, isSharing, ready)
                }

                LaunchedEffect(Unit) {
                    awaitFrame()
                    ready = true
                }
            }
        }
    }
}

private fun saveBitmapToGallery(context: Context, bmp: Bitmap): Uri? {
    val displayName = "FunQuizz_${System.currentTimeMillis()}.png"
    val mime = "image/png"

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, mime)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        ?: return null

    resolver.openOutputStream(uri)?.use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }
    return uri
}

private fun shareImage(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Partager"))
}

private fun ImageBitmap.toAndroidBitmap(): Bitmap {
    val width = this.width
    val height = this.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    canvas.drawBitmap(this.asAndroidBitmap(), 0f, 0f, paint)
    return bitmap
}

@Composable
fun ScreenShotView(screenshotState: ScreenshotState,category: Category, subCategory: SubCategory, level: Level, score: Int, isSharing:Boolean = false,ready:Boolean = false, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var isWaiting by remember { mutableStateOf(true) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    var backgroundStateColor by remember { mutableStateOf(backgroundColor) }
    var filterStateColor by remember { mutableStateOf(primaryColor) }
    var titleStateColor by remember { mutableStateOf(onPrimaryColor) }

    var backgroundRow1StateColor by remember { mutableStateOf(surfaceColor) }
    var onBackgroundRow1StateColor by remember { mutableStateOf(onSurfaceColor) }
    var backgroundRow1BoxStateColor by remember { mutableStateOf(primaryColor) }
    var onBackgroundRow1BoxStateColor by remember { mutableStateOf(onPrimaryColor) }

    var backgroundRow2StateColor by remember { mutableStateOf(surfaceColor) }
    var onBackgroundRow2StateColor by remember { mutableStateOf(onSurfaceColor) }
    var backgroundRow2BoxStateColor by remember { mutableStateOf(primaryColor) }
    var onBackgroundRow2BoxStateColor by remember { mutableStateOf(onPrimaryColor) }

    var backgroundRow3StateColor by remember { mutableStateOf(surfaceColor) }
    var onBackgroundRow3StateColor by remember { mutableStateOf(onSurfaceColor) }
    var backgroundRow3BoxStateColor by remember { mutableStateOf(primaryColor) }
    var onBackgroundRow3BoxStateColor by remember { mutableStateOf(onPrimaryColor) }

    var backgroundButtonRow4StateColor by remember { mutableStateOf(secondaryColor) }
    var onBackgroundButtonRow4StateColor by remember { mutableStateOf(onSecondaryColor) }
    var backgroundRoundedButtonRow4StateColor by remember { mutableStateOf(primaryColor) }
    var onBackgroundRoundedButtonRow4StateColor by remember { mutableStateOf(onPrimaryColor) }

    val timeToAppearMillis = 0
    val timeBeforeAppearanceMillis: (Int) -> Int = {
        timeToAppearMillis * (it%5)
    }
    val timeBeforeNextAnimationMillis = timeToAppearMillis * 5
    val timeForAnimationBackgroundRowMillis = if (isWaiting) 2500 else 500

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

    val backgroundRow1AnimateColor by animateColorAsState(
        targetValue = backgroundRow1StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow1AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow1StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRow1BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow1BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow1BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow1BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRow2AnimateColor by animateColorAsState(
        targetValue = backgroundRow2StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow2AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow2StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRow2BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow2BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow2BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow2BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRow3AnimateColor by animateColorAsState(
        targetValue = backgroundRow3StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow3AnimateColor by animateColorAsState(
        targetValue = onBackgroundRow3StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRow3BoxAnimateColor by animateColorAsState(
        targetValue = backgroundRow3BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRow3BoxAnimateColor by animateColorAsState(
        targetValue = onBackgroundRow3BoxStateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundButtonRow4AnimateColor by animateColorAsState(
        targetValue = backgroundButtonRow4StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundButtonRow4AnimateColor by animateColorAsState(
        targetValue = onBackgroundButtonRow4StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val backgroundRoundedButtonRow4AnimateColor by animateColorAsState(
        targetValue = backgroundRoundedButtonRow4StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val onBackgroundRoundedButtonRow4AnimateColor by animateColorAsState(
        targetValue = onBackgroundRoundedButtonRow4StateColor,
        animationSpec = tween(durationMillis = timeForAnimationBackgroundRowMillis)
    )

    val clickFunction : (Int) -> Unit = { index ->
        val intent = Intent(context, SubCategoryActivity::class.java)
        //intent.putExtra("Category", categories[index])
        context.startActivity(intent)
    }

    fun captureAndSave() {
        screenshotState.capture()

        val bitmap = screenshotState.bitmap ?: return
        val uri = saveBitmapToGallery(context, bitmap.asImageBitmap().toAndroidBitmap())
        //if (uri != null) shareImage(context, uri)
    }

    LaunchedEffect(ready) {
        screenshotState.capture()
        val bmp = screenshotState.bitmap ?: return@LaunchedEffect
        val uri = saveBitmapToGallery(context, bmp.asImageBitmap().toAndroidBitmap())
        Log.d("SCREEN", "uri = $uri")
        Toast.makeText(context, "saved : $uri", Toast.LENGTH_LONG).show()
        if (isSharing && uri != null) { shareImage(context, uri) }
    }

    ScreenshotBox(screenshotState = screenshotState) {
        Column (
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Logo
            //if (!isWaiting) {
            LogoImage(
                heightDp = 109.dp,
                widthDp = 287.dp,
                paddingValues = PaddingValues(top = 10.dp, bottom = 12.dp),
                isClickable = true,
                painterResourceId = R.drawable.funquizz_logo,
                colorFilter = onPrimaryColor,
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
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
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
                        .fillMaxHeight()
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
                        .fillMaxHeight()
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
                        .fillMaxHeight()
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

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    FunQuizzTheme {

        val screenshotState = rememberScreenshotState()
        ScreenShotView(screenshotState = screenshotState, category = Category(20, "dr"), subCategory = SubCategory(22, 20, "ed"), level = Level(203,"Facile"), score = 10)
    }
}