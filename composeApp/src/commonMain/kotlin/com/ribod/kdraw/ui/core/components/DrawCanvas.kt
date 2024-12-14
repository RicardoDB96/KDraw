package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import com.ribod.kdraw.domain.model.GlobalLine
import com.ribod.kdraw.ui.core.ex.drawingCanvasModifier
import com.ribod.kdraw.utils.adjustColorForBackground

@Composable
fun DrawCanvas(
    modifier: Modifier = Modifier,
    selectedTool: Tool,
    scale: Int,
    onScaleChange: (Int) -> Unit,
    offset: Offset,
    onOffsetChange: (Offset) -> Unit,
    width: Float,
    colorHex: Long,
    onDrawChange: (GlobalLine) -> Unit,
    onLinesMoved: (List<GlobalLine>) -> Unit,
    globalLines: List<GlobalLine>,
    canvasMode: CanvasMode,
) {
    val currentScale by rememberUpdatedState(scale)
    val currentOffset by rememberUpdatedState(offset)

    var movedLines by remember { mutableStateOf<List<GlobalLine>>(emptyList()) }

    var selectionBoundingBox: Rect? by remember { mutableStateOf(null) }

    val currentWidth by rememberUpdatedState(width)
    val currentColorHex by rememberUpdatedState(colorHex)
    val currentSelectedTool by rememberUpdatedState(selectedTool)

    val currentGlobalPath = remember { mutableStateListOf<Offset>() }

    var lastPoint by remember { mutableStateOf<Offset?>(null) }
    var isMovingSelection by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedLines by remember { mutableStateOf<List<GlobalLine>>(emptyList()) }

    var selectionStart by remember { mutableStateOf<Offset?>(null) }
    var selectionEnd by remember { mutableStateOf<Offset?>(null) }

    val globalLinesList by rememberUpdatedState(globalLines)
    val currentCanvasMode by rememberUpdatedState(canvasMode)

    val backgroundColor = MaterialTheme.colorScheme.background
    val selectionColor = if (isSystemInDarkTheme()) Color(0xFFAAC7FF) else Color(0xFF365E9D)

    Canvas(
        modifier = modifier
            .drawingCanvasModifier(
                offset = currentOffset,
                scale = currentScale,
                changeOffset = { newOffset, isSum ->
                    if (isSum) {
                        onOffsetChange(currentOffset + newOffset)
                    } else {
                        onOffsetChange(newOffset)
                    }
                },
                changeScale = { onScaleChange(it) },
                changeBoxOffSet = { newOffset, isSum ->
                    val allMovedPoints = movedLines.flatMap { line -> line.points }

                    val off = if (isSum) currentOffset + newOffset else newOffset

                    val movedCanvasPoints =
                        allMovedPoints.map { point -> globalToCanvas(point, currentScale.toFloat() / 100f, off) }

                    val minX = movedCanvasPoints.minOfOrNull { it.x } ?: 0f
                    val minY = movedCanvasPoints.minOfOrNull { it.y } ?: 0f
                    val maxX = movedCanvasPoints.maxOfOrNull { it.x } ?: 0f
                    val maxY = movedCanvasPoints.maxOfOrNull { it.y } ?: 0f

                    selectionBoundingBox = Rect(minX - 5f, minY - 5f, maxX + 5f, maxY + 5f)
                },
                changeBoxScale = { newScale ->
                    val allMovedPoints = movedLines.flatMap { line -> line.points }
                    val movedCanvasPoints =
                        allMovedPoints.map { point -> globalToCanvas(point, newScale.toFloat() / 100f, currentOffset) }

                    val minX = movedCanvasPoints.minOfOrNull { it.x } ?: 0f
                    val minY = movedCanvasPoints.minOfOrNull { it.y } ?: 0f
                    val maxX = movedCanvasPoints.maxOfOrNull { it.x } ?: 0f
                    val maxY = movedCanvasPoints.maxOfOrNull { it.y } ?: 0f

                    selectionBoundingBox = Rect(minX - 5f, minY - 5f, maxX + 5f, maxY + 5f)
                }
            )
            .fillMaxSize()
            .pointerInput(Unit) { // Zoom gesture
                detectTransformGestures(
                    onGesture = { centroid, _, zoom, _ ->
                        val newScale = (currentScale * zoom).toInt().coerceIn(10, 500)

                        if (newScale != currentScale) {
                            onScaleChange(newScale)
                            onOffsetChange((currentOffset - centroid) * zoom + centroid)
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { tapPosition ->
                    if (currentSelectedTool == Tool.Pen) {
                        val globalTap = canvasToGlobal(tapPosition, currentScale.toFloat() / 100f, currentOffset)
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
                        when (currentSelectedTool) {
                            Tool.Select -> {
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

                            Tool.Pen -> {
                                val globalStart = canvasToGlobal(startPosition, currentScale.toFloat() / 100f, currentOffset)
                                currentGlobalPath.clear()
                                currentGlobalPath.add(globalStart)
                                lastPoint = globalStart
                            }

                            else -> {}

                        }
                    },
                    onDrag = { change, dragAmount ->
                        when (currentSelectedTool) {
                            Tool.Select -> {
                                if (isMovingSelection) {
                                    val globalDragAmount =
                                        canvasToGlobal(dragAmount, currentScale.toFloat() / 100f, Offset.Zero) - Offset.Zero

                                    movedLines = movedLines.map { line ->
                                        val movedPoints = line.points.map { point ->
                                            point + globalDragAmount
                                        }
                                        line.copy(points = movedPoints)
                                    }

                                    onLinesMoved(movedLines)

                                    val allMovedPoints =
                                        movedLines.flatMap { line -> line.points.zip(List(line.points.size) { line.width }) }
                                    val movedCanvasPoints = allMovedPoints.map { (point, width) ->
                                        globalToCanvas(point, currentScale.toFloat() / 100f, currentOffset) to (width * currentScale.toFloat() / 100f)
                                    }

                                    val minX =
                                        movedCanvasPoints.minOfOrNull { (point, width) -> point.x - (width / 2) }
                                            ?: 0f
                                    val minY =
                                        movedCanvasPoints.minOfOrNull { (point, width) -> point.y - (width / 2) }
                                            ?: 0f
                                    val maxX =
                                        movedCanvasPoints.maxOfOrNull { (point, width) -> point.x + (width / 2) }
                                            ?: 0f
                                    val maxY =
                                        movedCanvasPoints.maxOfOrNull { (point, width) -> point.y + (width / 2) }
                                            ?: 0f

                                    selectionBoundingBox = Rect(minX, minY, maxX, maxY)
                                } else {
                                    selectionEnd = change.position
                                }
                            }

                            Tool.Pen -> {
                                change.consume()

                                val globalPoint = canvasToGlobal(change.position, currentScale.toFloat() / 100f, currentOffset)
                                currentGlobalPath.add(globalPoint)
                                lastPoint = globalPoint
                            }

                            Tool.Hand -> {
                                onOffsetChange(currentOffset + dragAmount)
                            }
                        }
                    },
                    onDragEnd = {
                        if (currentSelectedTool == Tool.Pen) {
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
                                                    globalToCanvas(point, currentScale.toFloat() / 100f, currentOffset)

                                                val pointRadius = (line.width / 2) * currentScale.toFloat() / 100f
                                                val pointRect = Rect(
                                                    canvasPoint.x - pointRadius,
                                                    canvasPoint.y - pointRadius,
                                                    canvasPoint.x + pointRadius,
                                                    canvasPoint.y + pointRadius
                                                )

                                                rect.overlaps(pointRect)
                                            }
                                        } else {
                                            line.points.all { point ->
                                                val canvasPoint =
                                                    globalToCanvas(point, currentScale.toFloat() / 100f, currentOffset)
                                                rect.contains(canvasPoint)
                                            }
                                        }
                                    }

                                    selectionBoundingBox = if (selectedLines.isNotEmpty()) {
                                        val allPoints =
                                            selectedLines.flatMap { line -> line.points }
                                        val canvasPoints = allPoints.map { point ->
                                            globalToCanvas(
                                                point,
                                                currentScale.toFloat() / 100f,
                                                currentOffset
                                            )
                                        }

                                        val minX = canvasPoints.minOfOrNull { it.x } ?: 0f
                                        val minY = canvasPoints.minOfOrNull { it.y } ?: 0f
                                        val maxX = canvasPoints.maxOfOrNull { it.x } ?: 0f
                                        val maxY = canvasPoints.maxOfOrNull { it.y } ?: 0f

                                        val maxLineWidth =
                                            selectedLines.maxOfOrNull { it.width } ?: 0f
                                        val adjustedLineWidth = (maxLineWidth * currentScale.toFloat() / 100f) / 2

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
        when (currentCanvasMode) {
            CanvasMode.GRID -> drawGrid(scale = currentScale.toFloat() / 100f, offset = currentOffset)
            CanvasMode.DOTS -> drawDots(scale = currentScale.toFloat() / 100f, offset = currentOffset)
            CanvasMode.EMPTY -> {}
        }

        // Dibujar todas las líneas: líneas originales no seleccionadas y las líneas seleccionadas (si se están moviendo)
        globalLinesList.forEach { line ->
            // Si estamos moviendo la selección, dibujar la nueva posición de las líneas seleccionadas
            if (isMovingSelection && selectedLines.contains(line)) {
                movedLines.find { it == line }?.let { movedLine ->
                    drawLineOnCanvas(movedLine, backgroundColor, currentScale.toFloat() / 100f, currentOffset)
                }
            } else {
                // Dibujar las líneas que no están siendo movidas o no están seleccionadas
                drawLineOnCanvas(line, backgroundColor, currentScale.toFloat() / 100f, currentOffset)
            }
        }

        selectionBoundingBox?.let { boundingBox ->
            // Ajustar el grosor del borde del recuadro según el zoom para que se mantenga visible
            val borderStrokeWidth = (2f / currentScale).coerceAtLeast(1f)

            drawRect(
                color = selectionColor,
                topLeft = Offset(boundingBox.left, boundingBox.top),
                size = Size(boundingBox.width, boundingBox.height),
                style = Stroke(width = borderStrokeWidth)
            )
        }

        if (currentGlobalPath.isNotEmpty()) {
            val realTimePath = Path().apply {
                val firstPoint = globalToCanvas(currentGlobalPath.first(), currentScale.toFloat() / 100f, currentOffset)
                moveTo(firstPoint.x, firstPoint.y)
                currentGlobalPath.drop(1).forEach { point ->
                    val canvasPoint = globalToCanvas(point, currentScale.toFloat() / 100f, currentOffset)
                    lineTo(canvasPoint.x, canvasPoint.y)
                }
            }

            //val tintedColor = tintColor(currentColorHex, currentTime)
            drawPath(
                path = realTimePath,
                color = adjustColorForBackground(
                    color = Color(currentColorHex),
                    backgroundColor = backgroundColor
                ),
                style = Stroke(
                    width = currentWidth * currentScale.toFloat() / 100f,
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
                    color = selectionColor,
                    topLeft = Offset(rect.left, rect.top),
                    size = Size(rect.width, rect.height),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

// Helper para dibujar las líneas
fun DrawScope.drawLineOnCanvas(
    line: GlobalLine,
    backgroundColor: Color,
    scale: Float,
    offset: Offset
) {
    if (line.isPoint) {
        val point = globalToCanvas(line.points.first(), scale, offset)
        drawCircle(
            color = adjustColorForBackground(line.color, backgroundColor = backgroundColor),
            radius = (line.width / 2) * scale,
            center = point
        )
    } else {
        for (i in 0 until line.points.size - 1) {
            val p1 = globalToCanvas(line.points[i], scale, offset)
            val p2 = globalToCanvas(line.points[i + 1], scale, offset)
            drawLine(
                color = adjustColorForBackground(line.color, backgroundColor = backgroundColor),
                start = p1,
                end = p2,
                strokeWidth = line.width * scale,
                cap = StrokeCap.Round
            )
        }
    }
}

// Conversión de coordenadas
fun canvasToGlobal(canvasPoint: Offset, scale: Float, offset: Offset): Offset {
    return (canvasPoint - offset) / scale
}

fun globalToCanvas(globalPoint: Offset, scale: Float, offset: Offset): Offset {
    return (globalPoint * scale) + offset
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
