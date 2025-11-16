package com.ever.funquizz.repository

import com.ever.funquizz.model.Category

class CategoryRepository {
    fun getCategories(categoriesStrings :List<String>): List<Category>{
        val categories : MutableList<Category> = mutableListOf()
        for ((id,category) in categoriesStrings.withIndex()){
            categories.add(Category(id+1,category))
        }
        return categories.toList()
    }
}