package com.ribod.kdraw.ui.main.tabs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ribod.kdraw.domain.DrawRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: DrawRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllDraws().collect { drawsList ->
                    _state.update { it.copy(draws = drawsList) }
                }
            }
        }
    }

    fun createNewDraw() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveDrawDB()
            }
        }
    }

    fun toggleSelection(drawId: Long) {
        _state.update {
            val selected = it.selectedDraws
            if (drawId in selected) {
                it.copy(selectedDraws = selected - drawId)
            } else {
                it.copy(selectedDraws = selected + drawId)
            }
        }
    }

    fun toggleFullSelection() {
        _state.update { state ->
            val selected = state.selectedDraws.size == state.draws.size
            if (selected) {
                state.copy(selectedDraws = emptySet())
            } else {
                state.copy(selectedDraws = state.draws.map { it.id }.toSet())
            }
        }
    }

    fun deleteSelectedDraws() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteDrawDB(ids = state.value.selectedDraws)
            }
            _state.update { it.copy(selectedDraws = emptySet()) }
        }
    }
}