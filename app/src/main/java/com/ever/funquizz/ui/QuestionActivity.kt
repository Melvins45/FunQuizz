package com.ever.funquizz.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ever.funquizz.MainActivity
import com.ever.funquizz.R
import com.ever.funquizz.model.BoxColors
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Party
import com.ever.funquizz.model.PartyRepository
import com.ever.funquizz.model.Response
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.ui.components.BottomRoundedButton
import com.ever.funquizz.ui.components.ButtonEndRow
import com.ever.funquizz.ui.components.ButtonStartRow
import com.ever.funquizz.ui.components.Loader
import com.ever.funquizz.ui.components.LogoImage
import com.ever.funquizz.ui.components.TextBox
import com.ever.funquizz.ui.components.TopRoundedButton
import com.ever.funquizz.ui.components.TransparentButton
import com.ever.funquizz.ui.theme.FunQuizzTheme
import com.ever.funquizz.ui.theme.onPrimaryActive
import com.ever.funquizz.ui.theme.primaryActive
import com.ever.funquizz.viewmodel.LevelViewModel
import com.ever.funquizz.viewmodel.QuestionViewModel
import com.ever.funquizz.viewmodel.ResponseViewModel
import kotlinx.coroutines.delay
import java.util.Date

class QuestionActivity : ComponentActivity() {
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
                    QuestionView(category, subCategory, level)
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun QuestionView(category: Category, subCategory: SubCategory, level : Level, modifier: Modifier = Modifier, questionViewModel: QuestionViewModel = viewModel(), responseViewModel: ResponseViewModel = viewModel()) {

    val context = LocalContext.current
    val isLoaded by questionViewModel.isLoaded.collectAsState()
    val questions by questionViewModel.questions.collectAsState()
    val responses by responseViewModel.responses.collectAsState()
    var currentQuestionId by remember { mutableStateOf(0) }
    var indexActive by remember { mutableStateOf(-1) }
    var score by remember { mutableStateOf(0) }
    var isChoosing by remember { mutableStateOf(true) }
    var hasFinished by remember { mutableStateOf(false) }
    val responsesMap = remember { mutableStateMapOf<Int, List<Response>>() }
    val textMeasurer = rememberTextMeasurer()

    // Colors for functions variables
    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorOnPrimary = MaterialTheme.colorScheme.onPrimary
    val colorPrimaryActive = MaterialTheme.colorScheme.primaryActive
    val colorOnPrimaryActive = MaterialTheme.colorScheme.onPrimaryActive
    val colorSecondary = MaterialTheme.colorScheme.secondary
    val colorOnSecondary = MaterialTheme.colorScheme.onSecondary
    val colorTertiary = MaterialTheme.colorScheme.tertiary
    val colorOnTertiary = MaterialTheme.colorScheme.onTertiary

    val startResponse = context.resources.getStringArray(R.array.start_response)

    val widthText : (String, TextStyle) -> Int = { text, textStyle ->
        textMeasurer.measure(
            text = AnnotatedString(text),
            style = textStyle
        ).size.width
    }

    val nameQuestion : (Int) -> String = {
        subCategory.nameSubCategory.lowercase().replace("é","e").replace("è","e").replace(" ","_")+"_"+level.nameLevel.lowercase()+"_question_"+it
    }
    val questionResId : (Int) -> Int = {
        context.resources.getIdentifier(nameQuestion(it), "string", context.packageName)
    }
    var questionsStrings : MutableList<String> = mutableListOf()
    /*for (i in 1..10){
        if (questionResId(i) != 0) {
            questionsStrings.add(context.resources.getString(questionResId(i)))
        }
    }*/

    val nameResponses : (Int) -> String = {
        nameQuestion(it)+"_reponses"
    }
    val nameIdResponseValid : (Int) -> String = {
        nameQuestion(it)+"_reponse_valide"
    }
    val responsesResId : (Int) -> Int = {
        context.resources.getIdentifier(nameResponses(it), "array", context.packageName)
    }
    val idResponseValidResId : (Int) -> Int = {
        context.resources.getIdentifier(nameIdResponseValid(it), "string", context.packageName)
    }
    val responseIdValid : (Int) -> Int = {
        context.getString(idResponseValidResId(it)).toInt()
    }
    val responsesStrings : (Int) -> List<String> = {
        context.resources.getStringArray(responsesResId(it)).toList()
    }

    // Choose the correct color to show depending on isChoosing or indexActive
    val responseContainerColor : (Int) -> Color = {
        if(isChoosing)
            if (it != indexActive)
                colorPrimary
            else
                colorPrimaryActive
        else
            if (it != indexActive)
                colorPrimary
            else
                if (responsesMap[currentQuestionId]?.get(it)?.isValid == true)
                    colorSecondary
                else
                    colorTertiary
    }

    // Choose the correct color to show depending on isChoosing or indexActive
    val responseContentColor : (Int) -> Color = {
        if(isChoosing)
            if (it != indexActive)
                colorOnPrimary
            else
                colorOnPrimaryActive
        else
            if (it != indexActive)
                colorOnPrimary
            else
                if (responsesMap[currentQuestionId]?.get(it)?.isValid == true)
                    colorOnSecondary
                else
                    colorOnTertiary
    }

    // Verify when enable the buttons when not choosing
    val responseEnabled : (Int) -> Boolean = {
        if(isChoosing)
            true
        else
            if (it == indexActive)
                true
            else
                responsesMap[currentQuestionId]?.get(it)?.isValid == true
    }

    // Verify if response is valid and not choosed then add suffix ✅
    val responseSuffix : (Int) -> String = {
        if(isChoosing)
            ""
        else
            if ((responsesMap[currentQuestionId]?.get(it)?.isValid == true) and (it != indexActive))
                " ✅"
            else
                ""
    }

    val clickResponseFunction : (Int) -> Unit = { index ->
        if(isChoosing) {
            indexActive = index
        }
    }

    val clickFunction : (Int) -> Unit = { index ->
        val intent = Intent(context, StartActivity::class.java)
        intent.putExtra("Category", category)
        intent.putExtra("SubCategory", subCategory)
        //intent.putExtra("Level", levels[index])
        context.startActivity(intent)
    }


    LaunchedEffect(Unit) {
        // 1) récupération des textes
        val list = mutableListOf<String>()
        for (i in 1..10) {
            val id = questionResId(i)
            if (id != 0) list += context.getString(id)
        }
        questionViewModel.loadQuestions(subCategory, level, list)
    }

    LaunchedEffect(isLoaded) {
        if (!isLoaded) return@LaunchedEffect
        questions.forEachIndexed { index, q ->
            responseViewModel.loadReponses(
                q,
                responseIdValid(index + 1),
                responsesStrings(index + 1)
            )
            responsesMap[index] = responseViewModel.responses.value
        }
    }

    /*LaunchedEffect(key1 = Unit, block = {
        for (i in 1..10){
            if (questionResId(i) != 0) {
                questionsStrings.add(context.getString(questionResId(i)))
                Log.d("TAG", "Valeur du string : ${context.getString(questionResId(i))}")
            } else {
                Log.e("TAG", "Ressource introuvable ${questionResId(i)}")
            }
        }
        Log.d("TAG", "questions strings : $questionsStrings")
        questionViewModel.loadQuestions(subCategory, level, questionsStrings.toList())
        delay(70)
        Log.d("TAG", "questions memes : $questions")
        if (questions.isNotEmpty()) {
            for (i in 1..10) {
                responseViewModel.loadReponses(questions[i-1], responseIdValid(i), responsesStrings(i))
                delay(70)
                responsesMap[i-1] = responses
            }
            Log.d("TAG", "reponses : $responsesMap")
        }
    })*/

    Log.d("TAG", "questions strings dehors : $questions")
    // Container for all elements
    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First Row of the container =  <   FQ   1/10
        Row (
            modifier = modifier
                .height(66.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button <
            TransparentButton(text = "<", onClick = { (context as Activity).finish() })
            Spacer(modifier = Modifier.weight(1f))

            //Logo
            LogoImage(
                heightDp = 66.dp,
                widthDp = 90.dp,
                isClickable = true,
                painterResourceId = R.drawable.funquizz_mini_logo,
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.weight(1f))

            // Number of questions
            Text(
                modifier = Modifier.padding(end= 12.dp),
                text = "${currentQuestionId+1}"+"/10",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Space Before Question Name
        Spacer(modifier = Modifier.height(9.dp))

        // Question Name
        TextBox(
            text = if (questions.isEmpty()) "UI"  else questions[currentQuestionId].question,
            modifier = modifier.fillMaxWidth(),
            heightDp = 140.dp,
            paddingValues = PaddingValues(horizontal = 20.dp),
            colors = BoxColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Column of Responses
        Column (
            modifier = modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            if (questions.isNotEmpty() && responsesMap.size == questions.size){
                //if (responsesMap.isNotEmpty()){
                Log.d("TAG", "reponses : $responsesMap")
                responsesMap?.get(currentQuestionId)?.withIndex()?.forEach{ (id,response) ->
                    // Each Response is a Bottom Rounded Button
                    BottomRoundedButton(
                        text = startResponse[id]+response.response + responseSuffix(id),
                        enabled = responseEnabled(id),
                        heightDp = if(widthText(startResponse[id]+response.response, MaterialTheme.typography.bodyLarge) > with(LocalDensity.current) { 270.dp.toPx() }) 75.dp else 55.dp,
                        widthDp = 270.dp,
                        onClick = { clickResponseFunction(id) },
                        colors = BoxColors(
                            containerColor = responseContainerColor(id),
                            contentColor = responseContentColor(id),
                        )
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            } else {
                Loader(color = MaterialTheme.colorScheme.primary)
            }
        }
        // Space before Button Valider
        Spacer(modifier = Modifier.height(5.dp))

        // Valider is a Top Rounded Button
        TopRoundedButton(
            text = if (hasFinished) context.getString(R.string.finish) else if (isChoosing) context.getString(R.string.valid) else context.getString(R.string.continuer),
            widthDp = 175.dp,
            heightDp = 45.dp,
            enabled = indexActive!=-1,
            colors = BoxColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = {
                if (isChoosing) {
                    isChoosing = false
                    if (responsesMap[currentQuestionId]?.get(indexActive)?.isValid == true) {
                        score ++
                    }
                    if (currentQuestionId == 9) {
                        hasFinished = true
                    }
                } else {
                    if (currentQuestionId == 9) {
                        val repo = PartyRepository(context)
                        val party = Party(
                            idParty = repo.nextId(),
                            category = category,
                            subCategory = subCategory,
                            level = level,
                            score = score,
                            date = Date()
                        )
                        repo.saveParty(party)

                        hasFinished = true
                        val intent = Intent(context, ScoreActivity::class.java)
                        intent.putExtra("Category", category)
                        intent.putExtra("SubCategory", subCategory)
                        intent.putExtra("Level", level)
                        intent.putExtra("Score", score)
                        context.startActivity(intent)
                    } else {
                        indexActive = -1
                        currentQuestionId++
                        isChoosing = true
                    }
                }
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    FunQuizzTheme {
        QuestionView(category = Category(20, "dr"), subCategory = SubCategory(22, 20, "ed"), level = Level(203,"Facile"))
    }
}