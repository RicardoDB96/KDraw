package com.ribod.kdraw.ui.draw

import androidx.lifecycle.ViewModel
import com.ribod.kdraw.domain.model.GlobalLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DrawViewModel : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    fun onDrawChange(globalLine: GlobalLine) {
        _state.update { it.copy(globalLines = it.globalLines.toMutableList().apply { add(globalLine) }) }
    }

    fun onLinesMoved(movedLines: List<GlobalLine>) {
        _state.update { state ->
            val updatedLines = state.globalLines.map { line ->
                movedLines.find { it.id == line.id } ?: line
            }
            state.copy(globalLines = updatedLines)
        }
    }

    fun onWidthChange(width: Float) {
        _state.update { it.copy(width = width) }
    }

    fun onColorChange(color: ULong) {
        _state.update { it.copy(color = color) }
    }
}