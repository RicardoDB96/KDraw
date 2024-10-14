package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.ribod.kdraw.ui.core.ex.drawingCanvasModifier
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun DrawingCanvas(
    globalLines: List<GlobalLine>,
    width: Float,
    colorHex: Long,
    onDrawChange: (GlobalLine) -> Unit,
    onLinesMoved: (List<GlobalLine>) -> Unit,
    modifier: Modifier = Modifier,
    isZoomEnabled: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    println(scale)

    var lastPoint by remember { mutableStateOf<Offset?>(null) }

    val currentWidth by rememberUpdatedState(width)
    val currentColorHex by rememberUpdatedState(colorHex)
    val zoomEnabled by rememberUpdatedState(isZoomEnabled)
    val globalLinesList by rememberUpdatedState(globalLines)

    var selectedLines by remember { mutableStateOf<List<GlobalLine>>(emptyList()) }

    var selectionStart by remember { mutableStateOf<Offset?>(null) }
    var selectionEnd by remember { mutableStateOf<Offset?>(null) }

    val currentGlobalPath = remember { mutableStateListOf<Offset>() }

    var canvasMode by remember { mutableStateOf(CanvasMode.EMPTY) }

    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isMovingSelection by remember { mutableStateOf(false) }

    var selectionBoundingBox: Rect? by remember { mutableStateOf(null) }

    var movedLines by remember { mutableStateOf<List<GlobalLine>>(emptyList()) }

    var currentTime by remember { mutableStateOf(0f) } // 0 a 1 para el ciclo día/noche
    var direction by remember { mutableStateOf(1) } // 1 para día, -1 para noche

    // Interpolación de colores
    val dayColor = Color(254, 241, 215) // Color del cielo diurno
    val nightColor = Color(26, 24, 19) // Color del cielo nocturno
    val backgroundColor = lerp(dayColor, nightColor, currentTime)

    // Corutina para actualizar el tiempo
    LaunchedEffect(Unit) {
        while (true) {
            currentTime += 0.01f * direction // Incrementar o decrementar el tiempo
            if (currentTime >= 1f) {
                currentTime = 1f
                direction = -1 // Cambia a dirección de noche
            } else if (currentTime <= 0f) {
                currentTime = 0f
                direction = 1 // Cambia a dirección de día
            }
            delay(100) // Esperar un tiempo para simular el paso del tiempo
        }
    }

    Box(Modifier.fillMaxSize()) {
        Canvas(
            modifier = modifier
                .background(backgroundColor)
                .drawingCanvasModifier(
                    offset = offset,
                    scale = scale,
                    changeOffset = { o, isSum ->
                        if (isSum) {
                            offset += o
                        } else {
                            offset = o
                        }
                    },
                    changeScale = {
                        scale = it
                    },
                    changeBoxOffSet = { o, isSum ->
                        val allMovedPoints = movedLines.flatMap { line -> line.points }

                        val off = if (isSum) offset + o else o

                        val movedCanvasPoints = allMovedPoints.map { point -> globalToCanvas(point, scale, off) }

                        val minX = movedCanvasPoints.minOfOrNull { it.x } ?: 0f
                        val minY = movedCanvasPoints.minOfOrNull { it.y } ?: 0f
                        val maxX = movedCanvasPoints.maxOfOrNull { it.x } ?: 0f
                        val maxY = movedCanvasPoints.maxOfOrNull { it.y } ?: 0f

                        selectionBoundingBox = Rect(minX - 5f, minY - 5f, maxX + 5f, maxY + 5f)
                    },
                    changeBoxScale = { scale ->
                        val allMovedPoints = movedLines.flatMap { line -> line.points }
                        val movedCanvasPoints = allMovedPoints.map { point -> globalToCanvas(point, scale, offset) }

                        val minX = movedCanvasPoints.minOfOrNull { it.x } ?: 0f
                        val minY = movedCanvasPoints.minOfOrNull { it.y } ?: 0f
                        val maxX = movedCanvasPoints.maxOfOrNull { it.x } ?: 0f
                        val maxY = movedCanvasPoints.maxOfOrNull { it.y } ?: 0f

                        selectionBoundingBox = Rect(minX - 5f, minY - 5f, maxX + 5f, maxY + 5f)
                    }
                )
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, _, zoom, _ ->
                            if (zoomEnabled) {
                                val newScale = (scale * zoom).coerceIn(0.5f, 30f)

                                if (newScale != scale) {
                                    scale = newScale
                                    offset = (offset - centroid) * zoom + centroid
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { tapPosition ->
                        if (!zoomEnabled) {
                            val globalTap = canvasToGlobal(tapPosition, scale, offset)
                            val newGlobalLine = GlobalLine(
                                points = listOf(globalTap),
                                width = currentWidth,
                                color = Color(currentColorHex),
                                isPoint = true
                            )
                            onDrawChange(newGlobalLine)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { startPosition ->
                            if (!zoomEnabled) {
                                val globalStart = canvasToGlobal(startPosition, scale, offset)
                                currentGlobalPath.clear()
                                currentGlobalPath.add(globalStart)
                                lastPoint = globalStart
                            } else {
                                if (selectionBoundingBox?.contains(startPosition) == true) {
                                    isMovingSelection = true
                                    dragOffset = Offset.Zero
                                    movedLines = selectedLines.map { it.copy() }
                                } else {
                                    selectedLines = emptyList()
                                    selectionStart = startPosition
                                    selectionEnd = null
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            if (!zoomEnabled) {
                                change.consume()

                                val globalPoint = canvasToGlobal(change.position, scale, offset)
                                currentGlobalPath.add(globalPoint)
                                lastPoint = globalPoint
                            } else {
                                if (isMovingSelection) {
                                    val globalDragAmount = canvasToGlobal(dragAmount, scale, Offset.Zero) - Offset.Zero

                                    movedLines = movedLines.map { line ->
                                        val movedPoints = line.points.map { point ->
                                            point + globalDragAmount
                                        }
                                        line.copy(points = movedPoints)
                                    }

                                    onLinesMoved(movedLines)

                                    val allMovedPoints = movedLines.flatMap { line -> line.points.zip(List(line.points.size) { line.width }) }
                                    val movedCanvasPoints = allMovedPoints.map { (point, width) ->
                                        globalToCanvas(point, scale, offset) to (width * scale)
                                    }

                                    val minX = movedCanvasPoints.minOfOrNull { (point, width) -> point.x - (width / 2) } ?: 0f
                                    val minY = movedCanvasPoints.minOfOrNull { (point, width) -> point.y - (width / 2) } ?: 0f
                                    val maxX = movedCanvasPoints.maxOfOrNull { (point, width) -> point.x + (width / 2) } ?: 0f
                                    val maxY = movedCanvasPoints.maxOfOrNull { (point, width) -> point.y + (width / 2) } ?: 0f

                                    selectionBoundingBox = Rect(minX, minY, maxX, maxY)
                                } else {
                                    selectionEnd = change.position
                                }
                            }
                        },
                        onDragEnd = {
                            if (!zoomEnabled) {
                                val newGlobalLine = GlobalLine(
                                    points = currentGlobalPath.toList(),
                                    width = currentWidth,
                                    color = Color(currentColorHex)
                                )
                                onDrawChange(newGlobalLine)

                                currentGlobalPath.clear()
                                lastPoint = null
                            } else if (isMovingSelection) {
                                isMovingSelection = false
                                onLinesMoved(movedLines)
                            } else {
                                selectionStart?.let { start ->
                                    selectionEnd?.let { end ->
                                        val rect = Rect(
                                            (start.x).coerceAtMost(end.x),
                                            (start.y).coerceAtMost(end.y),
                                            (start.x).coerceAtLeast(end.x),
                                            (start.y).coerceAtLeast(end.y)
                                        )

                                        selectedLines = globalLinesList.filter { line ->
                                            if (line.isPoint) {
                                                line.points.first().let { point ->
                                                    val canvasPoint =
                                                        globalToCanvas(point, scale, offset)

                                                    val pointRadius = (line.width / 2) * scale
                                                    val pointRect = Rect(
                                                        canvasPoint.x - pointRadius, canvasPoint.y - pointRadius,
                                                        canvasPoint.x + pointRadius, canvasPoint.y + pointRadius
                                                    )

                                                    rect.overlaps(pointRect)
                                                }
                                            } else {
                                                line.points.all { point ->
                                                    val canvasPoint =
                                                        globalToCanvas(point, scale, offset)
                                                    rect.contains(canvasPoint)
                                                }
                                            }
                                        }

                                        selectionBoundingBox = if (selectedLines.isNotEmpty()) {
                                            val allPoints = selectedLines.flatMap { line -> line.points }
                                            val canvasPoints = allPoints.map { point -> globalToCanvas(point, scale, offset) }

                                            val minX = canvasPoints.minOfOrNull { it.x } ?: 0f
                                            val minY = canvasPoints.minOfOrNull { it.y } ?: 0f
                                            val maxX = canvasPoints.maxOfOrNull { it.x } ?: 0f
                                            val maxY = canvasPoints.maxOfOrNull { it.y } ?: 0f

                                            val maxLineWidth = selectedLines.maxOfOrNull { it.width } ?: 0f
                                            val adjustedLineWidth = (maxLineWidth * scale) / 2

                                            Rect(
                                                minX - adjustedLineWidth,
                                                minY - adjustedLineWidth,
                                                maxX + adjustedLineWidth,
                                                maxY + adjustedLineWidth
                                            )
                                        } else {
                                            null
                                        }
                                    }
                                }
                                selectionStart = null
                                selectionEnd = null
                            }
                        }
                    )
                }
        ) {
            when (canvasMode) {
                CanvasMode.GRID -> drawGrid(tintColor(getGridColor(backgroundColor), currentTime), scale, offset)
                CanvasMode.DOTS -> drawDots(tintColor(getGridColor(backgroundColor), currentTime), scale, offset)
                CanvasMode.EMPTY -> { }
            }

            // Dibujar todas las líneas: líneas originales no seleccionadas y las líneas seleccionadas (si se están moviendo)
            globalLinesList.forEach { line ->
                // Si estamos moviendo la selección, dibujar la nueva posición de las líneas seleccionadas
                if (isMovingSelection && selectedLines.contains(line)) {
                    movedLines.find { it == line }?.let { movedLine ->
                        drawLineOnCanvas(movedLine, scale, offset, currentTime)
                    }
                } else {
                    // Dibujar las líneas que no están siendo movidas o no están seleccionadas
                    drawLineOnCanvas(line, scale, offset, currentTime)
                }
            }

            selectionBoundingBox?.let { boundingBox ->
                // Ajustar el grosor del borde del recuadro según el zoom para que se mantenga visible
                val borderStrokeWidth = (2f / scale).coerceAtLeast(1f)

                drawRect(
                    color = Color.Blue.copy(alpha = 0.3f),
                    topLeft = Offset(boundingBox.left, boundingBox.top),
                    size = Size(boundingBox.width, boundingBox.height),
                    style = Stroke(width = borderStrokeWidth)
                )
            }

            if (currentGlobalPath.isNotEmpty()) {
                val realTimePath = Path().apply {
                    val firstPoint = globalToCanvas(currentGlobalPath.first(), scale, offset)
                    moveTo(firstPoint.x, firstPoint.y)
                    currentGlobalPath.drop(1).forEach { point ->
                        val canvasPoint = globalToCanvas(point, scale, offset)
                        lineTo(canvasPoint.x, canvasPoint.y)
                    }
                }

                val tintedColor = tintColor(currentColorHex, currentTime)
                drawPath(
                    path = realTimePath,
                    color = tintedColor,
                    style = Stroke(
                        width = currentWidth * scale,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            selectionStart?.let { start ->
                selectionEnd?.let { end ->
                    val rect = Rect(
                        (start.x).coerceAtMost(end.x),
                        (start.y).coerceAtMost(end.y),
                        (start.x).coerceAtLeast(end.x),
                        (start.y).coerceAtLeast(end.y)
                    )
                    drawRect(
                        color = Color.Blue.copy(alpha = 0.3f),
                        topLeft = Offset(rect.left, rect.top),
                        size = Size(rect.width, rect.height),
                        style = Stroke(width = 2f)
                    )
                }
            }
        }

        Column(Modifier.align(Alignment.TopEnd)) {
            Button(
                onClick = { onModeChange(!zoomEnabled) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(if (zoomEnabled) "Dibujar" else "Zoom/Movimiento")
            }
            Button(onClick = { canvasMode = CanvasMode.GRID }) {
                Text("Modo Cuadrícula")
            }

            Button(onClick = { canvasMode = CanvasMode.DOTS }) {
                Text("Modo Puntos")
            }

            Button(onClick = { canvasMode = CanvasMode.EMPTY }) {
                Text("Modo Vacío")
            }
        }
    }
}

// Helper para dibujar las líneas
fun DrawScope.drawLineOnCanvas(line: GlobalLine, scale: Float, offset: Offset, currentTime: Float) {
    val tintedColor = tintColor(line.color, currentTime)

    if (line.isPoint) {
        val point = globalToCanvas(line.points.first(), scale, offset)
        drawCircle(
            color = tintedColor,
            radius = (line.width / 2) * scale,
            center = point
        )
    } else {
        for (i in 0 until line.points.size - 1) {
            val p1 = globalToCanvas(line.points[i], scale, offset)
            val p2 = globalToCanvas(line.points[i + 1], scale, offset)
            drawLine(
                color = tintedColor,
                start = p1,
                end = p2,
                strokeWidth = line.width * scale,
                cap = StrokeCap.Round
            )
        }
    }
}

// Función para aplicar tintado según el tiempo
fun tintColor(originalColor: Long, time: Float): Color {
    val r = (originalColor shr 16 and 0xFF).toInt()
    val g = (originalColor shr 8 and 0xFF).toInt()
    val b = (originalColor and 0xFF).toInt()

    // Define un factor de ajuste de color basado en el tiempo
    val nightFactor = when {
        time < 0.5 -> 1f // Día
        else -> 1f - ((time - 0.5f) * 0.5f) // Noche, oscurece el color
    }

    // Ajusta los colores
    val newR = (r * nightFactor).toInt().coerceIn(0, 255)
    val newG = (g * nightFactor).toInt().coerceIn(0, 255)
    val newB = (b * nightFactor).toInt().coerceIn(0, 255)

    return Color(newR, newG, newB)
}

fun tintColor(originalColor: Color, time: Float): Color {
    val r = (originalColor.red * 255).toInt()
    val g = (originalColor.green * 255).toInt()
    val b = (originalColor.blue * 255).toInt()

    // Define un factor de ajuste de color basado en el tiempo
    val nightFactor = when {
        time < 0.5 -> 1f // Día
        else -> 1f - ((time - 0.5f) * 0.5f) // Noche, oscurece el color
    }

    // Ajusta los colores
    val newR = (r * nightFactor).toInt().coerceIn(0, 255)
    val newG = (g * nightFactor).toInt().coerceIn(0, 255)
    val newB = (b * nightFactor).toInt().coerceIn(0, 255)

    return Color(newR / 255f, newG / 255f, newB / 255f)
}

// Función de interpolación
fun lerp(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = (start.red * (1 - fraction) + stop.red * fraction).coerceIn(0f, 1f),
        green = (start.green * (1 - fraction) + stop.green * fraction).coerceIn(0f, 1f),
        blue = (start.blue * (1 - fraction) + stop.blue * fraction).coerceIn(0f, 1f),
        alpha = 1f
    )
}

// Función para calcular el color inverso (claro u oscuro) según el color de fondo
fun getGridColor(backgroundColor: Color): Color {
    // Calcular la luminancia del color de fondo
    val luminance = 0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue
    return if (luminance < 0.5) Color.LightGray else Color.DarkGray // Si es oscuro, devuelve blanco; si es claro, devuelve negro
}

// Función para establecer la hora actual (de 0 a 23)
fun setTime(hour: Int) {
    //currentTime = (hour % 24) / 24f
}

// Clases de datos para almacenar líneas en el sistema de coordenadas global
data class GlobalLine(
    val id: String = UUID.randomUUID().toString(), // ID único
    val points: List<Offset>,
    val width: Float,
    val color: Color,
    val isPoint: Boolean = false
)

// Conversión de coordenadas
fun canvasToGlobal(canvasPoint: Offset, scale: Float, offset: Offset): Offset {
    return (canvasPoint - offset) / scale
}

fun globalToCanvas(globalPoint: Offset, scale: Float, offset: Offset): Offset {
    return (globalPoint * scale) + offset
}

enum class CanvasMode {
    EMPTY,
    GRID,
    DOTS
}

fun DrawScope.drawGrid(color: Color = Color.LightGray, scale: Float, offset: Offset) {
    val step = 50f * scale // Tamaño de cada celda ajustado por el zoom

    for (x in 0..(size.width / step).toInt()) {
        val xPos = x * step + offset.x % step
        drawLine(color, Offset(xPos, 0f), Offset(xPos, size.height))
    }

    for (y in 0..(size.height / step).toInt()) {
        val yPos = y * step + offset.y % step
        drawLine(color, Offset(0f, yPos), Offset(size.width, yPos))
    }
}

fun DrawScope.drawDots(color: Color = Color.LightGray, scale: Float, offset: Offset) {
    val step = 50f * scale // Espacio entre los puntos ajustado por el zoom
    val dotColor = color
    val dotRadius = 2f * scale // Tamaño del punto ajustado por el zoom

    for (x in 0..(size.width / step).toInt()) {
        for (y in 0..(size.height / step).toInt()) {
            val xPos = x * step + offset.x % step
            val yPos = y * step + offset.y % step
            drawCircle(dotColor, dotRadius, Offset(xPos, yPos))
        }
    }
}