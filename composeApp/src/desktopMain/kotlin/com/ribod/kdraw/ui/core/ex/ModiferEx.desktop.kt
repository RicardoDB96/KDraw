package com.ribod.kdraw.ui.core.ex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.drawingCanvasModifier(
    offset: Offset,
    scale: Float,
    changeOffset: (Offset, Boolean) -> Unit,
    changeScale: (Float) -> Unit,
    changeBoxOffSet: (Offset, Boolean) -> Unit,
    changeBoxScale: (Float) -> Unit
): Modifier =
    this.then(
        // Detectar la rueda del ratón para pan (desplazamiento)
        Modifier.pointerInput(Unit) {
            detectDragGestures(
                matcher = PointerMatcher.mouse(PointerButton.Tertiary),
            ) { dragAmount ->
                changeOffset(dragAmount, true)
                //offset += dragAmount

                changeBoxOffSet(dragAmount, true)
            }
        }
            // Detectar scroll y clic de rueda del mouse
            .onPointerEvent(PointerEventType.Scroll) { event ->
                val scrollDelta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                val isCtrlPressed = event.keyboardModifiers.isCtrlPressed
                if (isCtrlPressed) {
                    // Zoom con Ctrl + Scroll
                    val zoomChange = 1f - scrollDelta / 10f
                    val newScale = (scale * zoomChange).coerceIn(0.1f, 5f)

                    // Solo aplicar el nuevo scale si es diferente al actual
                    if (newScale != scale) {
                        changeScale(newScale)
                        //scale = newScale
                        changeBoxScale(newScale)


                        // Ajustar desplazamiento para hacer zoom hacia el centro del scroll
                        val centroid = event.changes.first().position
                        changeOffset((offset - centroid) * zoomChange + centroid, false)
                        //offset = (offset - centroid) * zoomChange + centroid
                        changeBoxOffSet((offset - centroid) * zoomChange + centroid, false)
                    }
                }
            }
    )