package com.ribod.kdraw.ui.draw

import androidx.compose.ui.graphics.Color
import com.ribod.kdraw.domain.model.GlobalLine

data class DrawState(
    val globalLines: List<GlobalLine> = emptyList(),
    val drawId: Long = 0,
    val width: Float = 1.0f,
    val color: ULong = Color.Black.value
)