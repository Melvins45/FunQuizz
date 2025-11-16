package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import com.ever.funquizz.model.Category
import com.ever.funquizz.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel : ViewModel() {
    private val repository = CategoryRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    /*init {
        loadCategories()
    }*/

    fun loadCategories(categoriesStrings: List<String>) {
        _categories.value = repository.getCategories(categoriesStrings)
    }
}
