package com.ribod.kdraw.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.ribod.kdraw.data.database.entity.LinesEntity
import com.ribod.kdraw.ui.core.components.canvasToGlobal
import java.util.UUID

data class GlobalLine(
    val id: String = UUID.randomUUID().toString(), // ID único
    val points: List<Offset>,
    val width: Float,
    val color: Color,
    val isPoint: Boolean = false,
    val drawId: Long
) {
    fun toEntity(): LinesEntity = LinesEntity(
        id = id,
        points = points,
        width = width,
        color = color,
        isPoint = isPoint,
        drawId = drawId
    )
}

fun GlobalLine.isNearPoint(
    touchPoint: Offset,
    offset: Offset,
    scale: Float,
    threshold: Float = 1f // Umbral para detección
): Boolean {
    if (points.isEmpty()) return false

    // Convertir la posición de toque a coordenadas globales
    val globalTouchPoint = canvasToGlobal(touchPoint, scale, offset)

    return if (isPoint) {
        // Si es un punto, calcular la distancia al punto
        val point = points.first()
        val distance = point.getDistanceTo(globalTouchPoint)
        distance <= (width / 2) * scale + threshold
    } else {
        // Si es un trazo, revisar la distancia a cada segmento
        points.zipWithNext { start, end ->
            val distance = distanceToSegment(globalTouchPoint, start, end)
            if (distance <= (width / 2) * scale + threshold) return true
        }
        false // No se encontró ningún segmento cerca
    }
}

fun Offset.getDistanceTo(other: Offset): Float {
    return (this - other).getDistance()
}


fun distanceToSegment(point: Offset, start: Offset, end: Offset): Float {
    // Vector del segmento: (end - start)
    val segment = end - start

    // Vector del punto relativo al inicio del segmento: (point - start)
    val pointToStart = point - start

    // Proyección escalar del punto en el segmento (normalizado entre 0 y 1)
    val segmentLengthSquared = segment.x * segment.x + segment.y * segment.y
    val t = if (segmentLengthSquared == 0f) {
        0f // Caso especial: el segmento es un punto (start == end)
    } else {
        (pointToStart.x * segment.x + pointToStart.y * segment.y) / segmentLengthSquared
    }.coerceIn(0f, 1f) // Asegurarse de que esté entre [0, 1]

    // Proyección del punto en el segmento
    val projection = Offset(
        start.x + t * segment.x,
        start.y + t * segment.y
    )

    // Distancia entre el punto y su proyección
    return (point - projection).getDistance()
}