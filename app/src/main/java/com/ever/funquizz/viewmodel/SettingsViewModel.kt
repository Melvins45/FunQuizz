package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ever.funquizz.model.Theme
import com.ever.funquizz.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    val music = repo.musicVolume.stateIn(viewModelScope, SharingStarted.Eagerly, 0.7f)
    val fx    = repo.fxVolume.stateIn(viewModelScope, SharingStarted.Eagerly, 0.8f)
    val theme = repo.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, Theme.SYSTEM)

    fun setTheme(t: Theme) = viewModelScope.launch { repo.setTheme(t) }

    fun saveMusic(v: Float) = viewModelScope.launch { repo.setMusic(v) }
    fun saveFx(v: Float)    = viewModelScope.launch { repo.setFx(v) }
}