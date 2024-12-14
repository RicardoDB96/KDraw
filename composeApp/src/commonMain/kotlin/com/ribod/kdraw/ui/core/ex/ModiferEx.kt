package com.ribod.kdraw.ui.core.ex

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
expect fun Modifier.drawingCanvasModifier(
    offset: Offset,
    scale: Int,
    changeOffset: (Offset, Boolean) -> Unit,
    changeScale: (Int) -> Unit,
    changeBoxOffSet: (Offset, Boolean) -> Unit,
    changeBoxScale: (Int) -> Unit
): Modifier