package com.ribod.kdraw.ui.core.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogState
import com.composables.core.Scrim
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenSettingsDialog(
    modifier: Modifier = Modifier,
    state: DialogState,
    onDismiss: () -> Unit,
    widthValue: Float,
    onWidthChange: (Float) -> Unit,
    onColorChange: (ULong) -> Unit
) {
    var width by remember { mutableStateOf(widthValue) }

    val colorController = rememberColorPickerController()

    Dialog(state = state, onDismiss = onDismiss) {
        Scrim(enter = fadeIn(), exit = fadeOut())
        DialogPanel(
            modifier = modifier
                .displayCutoutPadding()
                .systemBarsPadding()
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(20.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            enter = scaleIn(initialScale = 0.8f) + fadeIn(tween(durationMillis = 250)),
            exit = scaleOut(targetScale = 0.6f) + fadeOut(tween(durationMillis = 150)),
        ) {
            Column {
                Column(modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)) {
                    Row {
                        IconButton(onClick = {
                            width = (width - 1f).coerceIn(1f, 100f)
                            onWidthChange(width)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease width"
                            )

                        }
                        Slider(
                            modifier = Modifier.weight(weight = 1f, fill = false),
                            value = width,
                            onValueChange = { width = it },
                            valueRange = 1f..100f,
                            onValueChangeFinished = { onWidthChange(width) },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Transparent
                            ),
                            thumb = {
                                // Thumb personalizado
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            MaterialTheme.colorScheme.inverseSurface,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = width.toInt().toString(),
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        )
                        IconButton(onClick = {
                            width = (width + 1f).coerceIn(1f, 100f)
                            onWidthChange(width)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase width"
                            )
                        }
                    }
                    HsvColorPicker(
                        modifier = Modifier.padding(16.dp).fillMaxHeight(0.5f),
                        controller = colorController,
                        onColorChanged = { selectedColor: ColorEnvelope ->
                            onColorChange(selectedColor.color.value)
                        },
                    )
                    AlphaSlider(
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(35.dp),
                        controller = colorController,
                    )
                    BrightnessSlider(
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(35.dp),
                        controller = colorController,
                    )
                    AlphaTile(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        controller = colorController
                    )
                }
            }
        }
    }
}