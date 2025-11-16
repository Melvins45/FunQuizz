package com.ever.funquizz.repository

import com.ever.funquizz.model.Question
import com.ever.funquizz.model.Response

class ResponseRepository {

    fun getResponses(question: Question, validID : Int, reponsesStrings :List<String>): List<Response>{
        val responses : MutableList<Response> = mutableListOf()
        for ((id,reponse) in reponsesStrings.withIndex()){
            responses.add(Response(id+1,reponse, if (id==validID-1) true else false, question.idQuestion))
        }
        return responses.toList()
    }
}