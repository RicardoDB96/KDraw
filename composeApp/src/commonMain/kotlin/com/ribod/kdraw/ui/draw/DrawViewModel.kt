package com.ribod.kdraw.ui.draw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ribod.kdraw.domain.LinesRepository
import com.ribod.kdraw.domain.model.GlobalLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrawViewModel(private val linesRepository: LinesRepository, drawId: Long) : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(drawId = drawId) }
        viewModelScope.launch {
            linesRepository.getDrawDB(drawId).collect { lines ->
                _state.update { it.copy(globalLines = lines) }
            }
        }
    }

    fun onDrawChange(globalLine: GlobalLine) {
        viewModelScope.launch {
            linesRepository.saveLineDB(globalLine)
        }
    }

    fun onLinesMoved(movedLines: List<GlobalLine>) {
        viewModelScope.launch {
            linesRepository.updateLinesDB(movedLines)
        }
    }

    fun onLinesDeleted(deletedLines: List<GlobalLine>) {
        viewModelScope.launch {
            linesRepository.deleteLinesDB(deletedLines.map { it.id }.toSet())
        }
    }

    fun onWidthChange(width: Float) {
        _state.update { it.copy(width = width) }
    }

    fun onColorChange(color: ULong) {
        _state.update { it.copy(color = color) }
    }

    fun colorFromHexString(hex: String): Long? {
        // Eliminar el símbolo # si está presente
        val cleanHex = hex.removePrefix("#")

        // Verificar si el formato es AARRGGBB (8 caracteres) o RRGGBB (6 caracteres)
        val validHex = when (cleanHex.length) {
            6 -> "FF$cleanHex" // Si es RRGGBB, agregar 'FF' para la opacidad (255)
            8 -> cleanHex // Si es AARRGGBB, está listo para ser usado
            else -> return null // No es un formato válido
        }

        return try {
            // Convertir a Long y luego crear el objeto Color
            val colorLong = validHex.toLong(16)
            colorLong
        } catch (e: NumberFormatException) {
            null // En caso de que ocurra un error en la conversión
        }
    }
}