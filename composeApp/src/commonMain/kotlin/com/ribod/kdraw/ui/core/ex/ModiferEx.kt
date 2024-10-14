package com.ribod.kdraw.ui.core.ex

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
expect fun Modifier.drawingCanvasModifier(
    offset: Offset,
    scale: Float,
    changeOffset: (Offset, Boolean) -> Unit,
    changeScale: (Float) -> Unit,
    changeBoxOffSet: (Offset, Boolean) -> Unit,
    changeBoxScale: (Float) -> Unit
): Modifier