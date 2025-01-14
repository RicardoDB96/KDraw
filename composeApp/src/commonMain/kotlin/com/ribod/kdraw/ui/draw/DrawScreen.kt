package com.ribod.kdraw.ui.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ribod.kdraw.ui.core.components.DrawingCanvas
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DrawScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    vm: DrawViewModel
) {
    val state by vm.state.collectAsState()

    Scaffold(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            DrawingCanvas(
                globalLines = state.globalLines,
                width = state.width,
                onWidthChange = { width -> vm.onWidthChange(width) },
                colorHex = state.color,
                drawId = state.drawId,
                onColorChange = { color -> vm.onColorChange(color) },
                onDrawChange = { line -> vm.onDrawChange(line) },
                onLinesMoved = { lines -> vm.onLinesMoved(lines) },
                onLinesDeleted = { lines -> vm.onLinesDeleted(lines) }
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onBackPressed,
                    colors = IconButtonDefaults.iconButtonColors(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    }
}