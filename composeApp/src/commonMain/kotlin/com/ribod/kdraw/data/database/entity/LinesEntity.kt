package com.ribod.kdraw.data.database.entity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ribod.kdraw.domain.model.GlobalLine

@Entity
data class LinesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val points: List<Offset>,
    val width: Float,
    val color: Color,
    val isPoint: Boolean,
    val drawId: Long
) {
    fun toDomain(): GlobalLine = GlobalLine(
        id = id,
        points = points,
        width = width,
        color = color,
        isPoint = isPoint,
        drawId = drawId
    )
}