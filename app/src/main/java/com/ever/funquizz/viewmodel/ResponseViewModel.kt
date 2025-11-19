package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Question
import com.ever.funquizz.model.Response
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.QuestionRepository
import com.ever.funquizz.repository.ResponseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResponseViewModel : ViewModel() {
    private val repository = ResponseRepository()

    private val _responses = MutableStateFlow<List<Response>>(emptyList())
    val responses: StateFlow<List<Response>> = _responses

    /*init {
        loadCategories()
    }*/

    fun loadReponses(question: Question, validID : Int, reponsesStrings :List<String>) {
        _responses.value = repository.getResponses(question, validID, reponsesStrings)
    }
}