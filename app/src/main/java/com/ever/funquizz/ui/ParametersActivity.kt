package com.ever.funquizz.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.factory.SettingsViewModelFactory
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.model.SoundManager
import com.ever.funquizz.model.Theme
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.FlatSlider
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.components.OnOffSwitch
import com.ever.funquizz.ui.components.RoundedRow
import com.ever.funquizz.ui.components.TopStartRoundedButton
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.utils.resolveDarkTheme
import com.ever.funquizz.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ParametersActivity : ComponentActivity() {

    private val settingsRepo by lazy { SettingsRepository(applicationContext) }

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val userTheme by settingsVm.theme.collectAsState()
            FunQuizzTheme(theme = userTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParametersView(viewModel = settingsVm)
                }
            }
        }
    }
}



@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun ParametersView(viewModel: SettingsViewModel?, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val formatDate = SimpleDateFormat(
        "dd/MM/yyyy - HH'h'mm",
        Locale.getDefault()
    )

    val music by viewModel?.music?.collectAsState() ?: produceState(0.7f) {  }
    val fx by viewModel?.fx?.collectAsState() ?: produceState(0.7f) {  }
    val theme by viewModel?.theme?.collectAsState() ?: produceState(Theme.SYSTEM) {  }

    val clickFunction : (Int) -> Unit = { index ->
        val intent = Intent(context, SubCategoryActivity::class.java)
        //intent.putExtra("Category", categories[index])
        context.startActivity(intent)
    }

    LaunchedEffect(key1 = Unit, block = {

    })

    LaunchedEffect(music) { SoundManager.setBackgroundVolume(music) }

    DisposableEffect(Unit) {
        if (!SoundManager.isBackgroundPlaying) SoundManager.playBackground(context, R.raw.background, music)
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
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(25.dp))

        ButtonStartRow(
            text = context.getString(R.string.parameters),
            clickable = false,
            onClick = { /*TODO*/ },
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Scrollable Column for items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
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
                    text = context.getString(R.string.music),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FlatSlider(
                widthDp = 265.dp,
                heightDp = 20.dp,
                value = music,
                onValue = { viewModel?.saveMusic(it) }
            )

            Spacer(modifier = Modifier.height(45.dp))

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
                    text = context.getString(R.string.effefts),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FlatSlider(
                widthDp = 265.dp,
                heightDp = 20.dp,
                value = fx,
                onValue = { viewModel?.saveFx(it) }
            )

            Spacer(modifier = Modifier.height(45.dp))

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
                    text = context.getString( if (theme == Theme.LIGHT) R.string.day else if (theme == Theme.SYSTEM && !isSystemInDarkTheme()) R.string.day else R.string.night),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OnOffSwitch(
                widthDp = 70.dp,
                heightDp = 30.dp,
                checked = if (theme == Theme.LIGHT) true else theme == Theme.SYSTEM && !isSystemInDarkTheme(),
                onChecked = {
                    if ( it ) {
                        viewModel?.setTheme(Theme.LIGHT)
                    } else {
                        viewModel?.setTheme(Theme.DARK)
                    }
                }
            )

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
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(intent)
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview8() {
    FunQuizzTheme {
        ParametersView(null)
    }
}