package com.ever.funquizz.repository

import com.ever.funquizz.model.Level
import com.ever.funquizz.model.Question
import com.ever.funquizz.model.SubCategory

class QuestionRepository {

    fun getQuestions(subCategory: SubCategory, level : Level, questionsStrings :List<String>): List<Question>{
        val questions : MutableList<Question> = mutableListOf()
        for ((id, question) in questionsStrings.withIndex()){
            questions.add(Question(id+1, question, subCategory.idSubCategory, level.idLevel))
        }
        return questions.toList()
    }
}