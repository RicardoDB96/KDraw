package com.ribod.kdraw.ui.draw

import com.ribod.kdraw.domain.model.GlobalLine

data class DrawState(
    val globalLines: List<GlobalLine> = emptyList(),
    val width: Float = 1.0f,
    val color: Long = 0xFF000000
)