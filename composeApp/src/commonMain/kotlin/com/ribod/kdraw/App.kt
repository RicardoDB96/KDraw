package com.ribod.kdraw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ribod.kdraw.ui.core.components.DrawingCanvas
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(vm: AppViewModel = viewModel { AppViewModel() }) {
    MaterialTheme {
        val state by vm.uiState.collectAsState()

        var width by rememberSaveable { mutableStateOf(2f) }
        var color by rememberSaveable { mutableStateOf("") }
        var isZoomEnable by rememberSaveable { mutableStateOf(false) }

        Column(Modifier.fillMaxWidth()) {
            Slider(value = width, onValueChange = { width = it }, valueRange = 1f..100f)
            OutlinedTextField(value = color, onValueChange = { color = it }, prefix = { Text("#") })

            DrawingCanvas(
                //modifier = Modifier.weight(1f),
                globalLines = state.globalLines,
                width = width,
                colorHex = vm.colorFromHexString(color) ?: 0xFF000000,
                isZoomEnabled = isZoomEnable,
                onModeChange = { isZoomEnable = it },
                onDrawChange = { vm.onDrawChange(it) },
                onLinesMoved = { vm.onLinesMoved(it) }
            )
        }
    }
}