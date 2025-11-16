package com.ever.funquizz.model

data class Response(
    val idReponse:Int,
    val reponse:String,
    val isValid : Boolean,
    val idQuestion : Int
) : java.io.Serializable
