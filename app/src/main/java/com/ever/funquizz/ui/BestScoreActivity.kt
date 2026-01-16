package com.ever.funquizz.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.factory.PartyViewModelFactory
import com.ever.funquizz.factory.SettingsViewModelFactory
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Party
import com.ever.funquizz.model.PartyRepository
import com.ever.funquizz.model.SoundManager
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.components.BottomEndRoundedButton
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.LetterPerLetterAnim
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.components.RoundedColumn
import com.ever.funquizz.ui.components.RoundedRow
import com.ever.funquizz.ui.components.TextBox
import com.ever.funquizz.ui.components.TopRoundedButton
import com.ever.funquizz.ui.components.TopStartRoundedButton
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.viewmodel.PartyViewModel
import com.ever.funquizz.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

class BestScoreActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(SoundManager)

        val viewModel = ViewModelProvider(
            this,
            PartyViewModelFactory(PartyRepository(this))
        )[PartyViewModel::class.java]

        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val parties by viewModel.parties.collectAsState()
                    BestScoreView(parties = parties, settingsVm =  settingsVm)
                }
            }
        }
    }
}



@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun BestScoreView(parties:List<Party> = listOf(), modifier: Modifier = Modifier, settingsVm: SettingsViewModel? = null) {

    val context = LocalContext.current
    val formatDate = SimpleDateFormat(
        "dd/MM/yyyy - HH'h'mm",
        Locale.getDefault()
    )
    val musicVol by settingsVm?.music?.collectAsState() ?: produceState(0.7f) {  }
    val fxVol by settingsVm?.fx?.collectAsState() ?: produceState(0.7f) {  }

    val clickFunction : (Int) -> Unit = { index ->
        val intent = Intent(context, SubCategoryActivity::class.java)
        //intent.putExtra("Category", categories[index])
        context.startActivity(intent)
    }

    LaunchedEffect(key1 = Unit, block = {

    })

    DisposableEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, musicVol)
        onDispose { /*SoundManager.stopBackground()*/ }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Logo
        LogoImage(
            heightDp = 36.dp,
            widthDp = 90.dp,
            paddingValues = PaddingValues(top = 18.dp, bottom = 12.dp),
            isClickable = true,
            painterResourceId = R.drawable.funquizz_mini_logo,
            colorFilter = MaterialTheme.colorScheme.onPrimary,
            onClick = {
                SoundManager.playSound(context, R.raw.click, fxVol)
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        )
        
        Spacer(modifier = Modifier.height(25.dp))
        
        ButtonStartRow(
            text = context.getString(R.string.best_score),
            clickable = false,
            onClick = { /*TODO*/ },
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Scrollable Column for items
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(parties) { party ->
                // Show each party
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                    RoundedRow (
                        modifier = Modifier
                            .padding(bottom = 5.dp),
                        cornerShape = RoundedCornerShape(topStart = 55.dp, topEnd = 55.dp),
                        heightDp = 55.dp,
                        widthDp = 210.dp,
                        colors = BoxColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = party.score.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                        Text(
                            text = party.category.nameCategory + " - " + party.subCategory.nameSubCategory,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = party.level.nameLevel,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formatDate.format(party.date),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // Button at end
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TopStartRoundedButton(
                widthDp = 175.dp,
                heightDp = 45.dp,
                text = context.getString(R.string.retourner) + " >",
                textStyle = MaterialTheme.typography.bodySmall,
                onClick = {
                    SoundManager.playSound(context, R.raw.click, fxVol)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(intent)
                },)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview9() {
    FunQuizzTheme {
        BestScoreView()
    }
}