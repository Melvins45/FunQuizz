package com.ever.funquizz.repository

import com.ever.funquizz.model.Category
import com.ever.funquizz.model.SubCategory


class SubCategoryRepository {

    fun getSubCategories(category: Category, subCategoriesStrings :List<String>): List<SubCategory>{
        val subCategories : MutableList<SubCategory> = mutableListOf()
        for ((id,subCategory) in subCategoriesStrings.withIndex()){
            subCategories.add(SubCategory(id+1,category.idCategory, subCategory))
        }
        return subCategories.toList()
    }
}