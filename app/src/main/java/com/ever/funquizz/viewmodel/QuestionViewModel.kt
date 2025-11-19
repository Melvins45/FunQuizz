package com.ever.funquizz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Question
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.QuestionRepository
import com.ever.funquizz.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuestionViewModel : ViewModel() {
    private val repository = QuestionRepository()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    /*init {
        loadCategories()
    }*/

    fun loadQuestions(subCategory: SubCategory, level: Level, questionsStrings :List<String>) {
        Log.d("Get", "${repository.getQuestions(subCategory, level, questionsStrings)}")
        _questions.value = repository.getQuestions(subCategory, level, questionsStrings)
        Log.d("Get", "${questions.value}")
    }
}