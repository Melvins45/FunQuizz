package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import com.ever.funquizz.model.Category
import com.ever.funquizz.model.Level
import com.ever.funquizz.repository.CategoryRepository
import com.ever.funquizz.repository.LevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LevelViewModel : ViewModel() {
    private val repository = LevelRepository()

    private val _levels = MutableStateFlow<List<Level>>(emptyList())
    val levels: StateFlow<List<Level>> = _levels

    /*init {
        loadCategories()
    }*/

    fun loadLevels(levelsStrings: List<String>) {
        _levels.value = repository.getLevels(levelsStrings)
    }
}