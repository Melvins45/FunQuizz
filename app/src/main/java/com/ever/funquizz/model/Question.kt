package com.ever.funquizz.model

data class Question(
    val idQuestion:Int,
    val question: String,
    val idSubCategory: Int,
    val idLevel: Int
) : java.io.Serializable