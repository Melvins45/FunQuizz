package com.ever.funquizz.model

data class Response(
    val idResponse:Int,
    val response:String,
    val isValid : Boolean,
    val idQuestion : Int
) : java.io.Serializable
