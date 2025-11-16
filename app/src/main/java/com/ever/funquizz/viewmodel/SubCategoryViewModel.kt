package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.SubCategory
import com.ever.funquizz.repository.CategoryRepository
import com.ever.funquizz.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SubCategoryViewModel : ViewModel() {
    private val repository = SubCategoryRepository()

    private val _subCategories = MutableStateFlow<List<SubCategory>>(emptyList())
    val subCategories: StateFlow<List<SubCategory>> = _subCategories

    /*init {
        loadCategories()
    }*/

    fun loadCategories(category: Category, subCategoriesStrings: List<String>) {
        _subCategories.value = repository.getSubCategories(category, subCategoriesStrings)
    }
}
