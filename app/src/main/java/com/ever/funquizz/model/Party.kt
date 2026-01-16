package com.ever.funquizz.model

import java.util.Date

data class Party (
    val idParty: Int,
    val category: Category,
    val subCategory: SubCategory,
    val level: Level,
    val score: Int,
    val date: Date
    ) : java.io.Serializable