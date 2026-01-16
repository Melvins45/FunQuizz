package com.ever.funquizz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ever.funquizz.model.Party
import com.ever.funquizz.model.PartyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PartyViewModel(
    private val repo: PartyRepository
) : ViewModel() {

    private val _parties = MutableStateFlow<List<Party>>(emptyList())
    val parties: StateFlow<List<Party>> = _parties.asStateFlow()

    init {
        loadParties()
    }

    private fun loadParties() = viewModelScope.launch {
        _parties.value = repo.getAllParties()
    }

}