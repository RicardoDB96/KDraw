package com.ribod.kdraw

import androidx.lifecycle.ViewModel
import com.ribod.kdraw.ui.core.components.GlobalLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val globalLines: List<GlobalLine> = emptyList(),
    val width: Float = 1.0f,
    val color: Long = 0xFF000000
)

class AppViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun onDrawChange(globalLine: GlobalLine) {
        _uiState.update { it.copy(globalLines = it.globalLines.toMutableList().apply { add(globalLine) }) }
    }

    fun onLinesMoved(movedLines: List<GlobalLine>) {
        _uiState.update { state ->
            val updatedLines = state.globalLines.map { line ->
                movedLines.find { it.id == line.id } ?: line
            }
            state.copy(globalLines = updatedLines)
        }
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