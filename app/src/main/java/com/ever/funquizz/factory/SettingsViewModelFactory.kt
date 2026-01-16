package com.ever.funquizz.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ever.funquizz.repository.SettingsRepository
import com.ever.funquizz.viewmodel.SettingsViewModel

class SettingsViewModelFactory(
    private val repo: SettingsRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SettingsViewModel(repo) as T
}