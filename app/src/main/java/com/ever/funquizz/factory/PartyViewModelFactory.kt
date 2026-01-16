package com.ever.funquizz.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ever.funquizz.model.PartyRepository
import com.ever.funquizz.viewmodel.PartyViewModel

class PartyViewModelFactory(
    private val repo: PartyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PartyViewModel(repo) as T
}