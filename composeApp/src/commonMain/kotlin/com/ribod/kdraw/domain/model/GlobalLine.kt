package com.ribod.kdraw.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.UUID

data class GlobalLine(
    val id: String = UUID.randomUUID().toString(), // ID único
    val points: List<Offset>,
    val width: Float,
    val color: Color,
    val isPoint: Boolean = false
)