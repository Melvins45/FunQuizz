package com.ever.funquizz.model

data class Reponse(
    val idReponse:Int,
    val reponse:String,
    val isValid : Boolean,
    val idQuestion : Int
) : java.io.Serializable
