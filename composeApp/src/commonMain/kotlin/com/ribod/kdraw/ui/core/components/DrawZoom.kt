package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun DrawZoom(
    modifier: Modifier = Modifier,
    zoomPercent: Float,
    onPercentageClick: () -> Unit,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .sizeIn(
                maxHeight = 48.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ZoomButton(
                icon = Icons.Default.Remove,
                description = "Zoom out",
                onClick = onZoomOut
            )
            PercentageAndResetZoom(zoomPercent = zoomPercent, onPercentageClick = onPercentageClick)
            ZoomButton(
                icon = Icons.Default.Add,
                description = "Zoom in",
                onClick = onZoomIn
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentageAndResetZoom(modifier: Modifier = Modifier, zoomPercent: Float, onPercentageClick: () -> Unit) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(text = "Reset zoom") } },
        state = rememberTooltipState(),
    ) {
        Text(
            text = "${zoomPercent.times(100).toInt()}%",
            modifier = modifier.clickable(role = Role.Button, onClick = onPercentageClick)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ZoomButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = description)
    }
}