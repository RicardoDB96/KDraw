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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
    colorHex: Long,
    onDrawChange: (GlobalLine) -> Unit,
    onLinesMoved: (List<GlobalLine>) -> Unit,
) {
    var toolSelected by rememberSaveable { mutableStateOf(Tool.Hand) }
    var canvasMode by rememberSaveable { mutableStateOf(CanvasMode.EMPTY) }

    Box(modifier = modifier.fillMaxSize()) {
        DrawCanvas(
            modifier = Modifier.fillMaxSize(),
            selectedTool = toolSelected,
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
                .padding(bottom = 16.dp),
            toolSelected = toolSelected,
            onToolClick = { toolSelected = it },
            canvasMode = canvasMode,
            onCanvasModeChange = { canvasMode = it }
        )
    }
}
