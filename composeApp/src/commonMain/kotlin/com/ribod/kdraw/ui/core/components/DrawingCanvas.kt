package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.composables.core.rememberDialogState
import com.ribod.kdraw.domain.model.GlobalLine
import com.ribod.kdraw.ui.core.icons.DotsIcon
import com.ribod.kdraw.ui.core.icons.SelectIcon

enum class CanvasMode(val icon: ImageVector, val actionText: String) {
    EMPTY(icon = Icons.Default.CropSquare, actionText = "Empty"),
    GRID(icon = Icons.Default.GridOn, actionText = "Grid"),
    DOTS(icon = DotsIcon, actionText = "Dots"),
}

enum class Tool(val icon: ImageVector, val actionText: String) {
    Hand(icon = Icons.Default.BackHand, actionText = "Hand"),
    Select(icon = SelectIcon, actionText = "Select"),
    Pen(icon = Icons.Default.Edit, actionText = "Pen"),
}

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    globalLines: List<GlobalLine>,
    width: Float,
    onWidthChange: (Float) -> Unit,
    colorHex: ULong,
    onColorChange: (ULong) -> Unit,
    onDrawChange: (GlobalLine) -> Unit,
    onLinesMoved: (List<GlobalLine>) -> Unit,
) {
    var toolSelected by rememberSaveable { mutableStateOf(Tool.Hand) }
    var canvasMode by rememberSaveable { mutableStateOf(CanvasMode.EMPTY) }
    var showPenSettings by rememberSaveable { mutableStateOf(false) }

    var scale by remember { mutableStateOf(100) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var size by remember { mutableStateOf(Size.Zero) }

    val penSettingsDialogState = rememberDialogState(initiallyVisible = false)

    Box(modifier = modifier.fillMaxSize()) {
        DrawCanvas(
            modifier = Modifier.fillMaxSize()
                .onSizeChanged { canvasSize ->
                    size = Size(canvasSize.width.toFloat(), canvasSize.height.toFloat())
                },
            selectedTool = toolSelected,
            scale = scale,
            onScaleChange = { scale = it },
            offset = offset,
            onOffsetChange = { offset = it },
            width = width,
            colorHex = colorHex,
            onDrawChange = { onDrawChange(it) },
            onLinesMoved = { onLinesMoved(it) },
            globalLines = globalLines,
            canvasMode = canvasMode
        )

        DrawToolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            toolSelected = toolSelected,
            onToolClick = {
                if (toolSelected == Tool.Pen && it == Tool.Pen) {
                    showPenSettings = !showPenSettings
                    penSettingsDialogState.visible = showPenSettings
                } else {
                    toolSelected = it
                }
            },
            canvasMode = canvasMode,
            onCanvasModeChange = { canvasMode = it }
        )

        DrawZoom(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            zoomPercent = scale,
            onPercentageClick = {
                val newScale = 100
                if (newScale != scale) {
                    val centerOfCanvas = Offset(size.width / 2f, size.height / 2f)
                    offset =
                        (offset - centerOfCanvas) * (newScale / scale.toFloat()) + centerOfCanvas
                    scale = newScale
                }
            },
            onZoomIn = {
                val newScale = (scale + 10).coerceIn(10, 500)
                if (newScale != scale) {
                    val centerOfCanvas = Offset(size.width / 2f, size.height / 2f)
                    offset =
                        (offset - centerOfCanvas) * (newScale / scale.toFloat()) + centerOfCanvas
                    scale = newScale
                }
            },
            onZoomOut = {
                val newScale = (scale - 10).coerceIn(10, 500)
                if (newScale != scale) {
                    val centerOfCanvas = Offset(size.width / 2f, size.height / 2f)
                    offset =
                        (offset - centerOfCanvas) * (newScale / scale.toFloat()) + centerOfCanvas
                    scale = newScale
                }
            }
        )

        PenSettingsDialog(
            state = penSettingsDialogState,
            onDismiss = { showPenSettings = false },
            widthValue = width,
            onWidthChange = { onWidthChange(it) },
            colorValue = colorHex,
            onColorChange = { onColorChange(it) }
        )
    }
}
