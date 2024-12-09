package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawToolbar(
    modifier: Modifier = Modifier,
    toolSelected: Tool,
    onToolClick: (Tool) -> Unit,
    canvasMode: CanvasMode,
    onCanvasModeChange: (CanvasMode) -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .sizeIn(
                maxHeight = 48.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tool.entries.forEach { tool ->
                ToolButton(
                    tool = tool,
                    toolSelected = toolSelected,
                    onToolClick = { onToolClick(tool) }
                )
            }
            VerticalDivider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                color = MaterialTheme.colorScheme.inversePrimary
            )
            CanvasModeButton(
                canvasMode = canvasMode,
                onCanvasModeChange = { onCanvasModeChange(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolButton(
    modifier: Modifier = Modifier,
    tool: Tool,
    toolSelected: Tool,
    onToolClick: (Tool) -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(text = tool.actionText) } },
        state = rememberTooltipState(),
    ) {
        Box(
            modifier = modifier.clip(MaterialTheme.shapes.small).background(
                if (toolSelected == tool) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else Color.Transparent
            ).clickable(onClick = { onToolClick(tool) })
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = tool.actionText,
                tint = if (toolSelected == tool) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasModeButton(
    modifier: Modifier = Modifier,
    canvasMode: CanvasMode,
    onCanvasModeChange: (CanvasMode) -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(text = canvasMode.actionText) } },
        state = rememberTooltipState(),
    ) {
        Box(
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = {
                    onCanvasModeChange(CanvasMode.entries[(canvasMode.ordinal + 1) % CanvasMode.entries.size])
                })
        ) {
            Icon(
                imageVector = canvasMode.icon,
                contentDescription = canvasMode.actionText,
                tint = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}