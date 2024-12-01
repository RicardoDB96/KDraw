package com.ribod.kdraw.ui.main.tabs.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.core.rememberDialogState
import com.ribod.kdraw.domain.model.DrawModel
import com.ribod.kdraw.ui.core.components.DeleteDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToDraw: (Long) -> Unit,
    vm: HomeViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()
    val deleteDialogState = rememberDialogState(initiallyVisible = false)

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar(
                isSelectableMode = state.selectedDraws.isEmpty(),
                draws = state.draws,
                selectedDraws = state.selectedDraws,
                onIconClick = { vm.toggleFullSelection() },
                onDeleteClick = { deleteDialogState.visible = true }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { vm.createNewDraw() },
                text = { Text("New Draw") },
                icon = {
                    Icon(
                        Icons.Default.Add, contentDescription = "New Draw"
                    )
                },
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(items = state.draws, key = { it.id }) { item ->
                    DrawCardItem(
                        drawModel = item,
                        isSelected = item.id in state.selectedDraws,
                        isSelectable = state.selectedDraws.isNotEmpty(),
                        onItemClicked = { navigateToDraw(item.id) },
                        onItemLongClicked = { vm.toggleSelection(item.id) }
                    )
                }
            }
        }

        DeleteDialog(state = deleteDialogState, onDeleteClick = { vm.deleteSelectedDraws() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    isSelectableMode: Boolean,
    draws: List<DrawModel>,
    selectedDraws: Set<Long>,
    onIconClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (isSelectableMode) {
        TopAppBar(modifier = modifier, title = { Text("Draws") })
    } else {
        TopAppBar(
            modifier = modifier,
            title = { Text("${selectedDraws.size} selected") },
            navigationIcon = {
                ClickableCheckCircle(
                    isSelected = draws.size == selectedDraws.size,
                    onIconClick = onIconClick
                )
            },
            actions = {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete draw",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawCardItem(
    modifier: Modifier = Modifier,
    drawModel: DrawModel,
    isSelected: Boolean,
    isSelectable: Boolean,
    onItemClicked: () -> Unit,
    onItemLongClicked: () -> Unit,
) {
    val selectedModifier = if (isSelected) modifier.border(
        border = BorderStroke(width = 6.dp, color = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) else modifier

    Column(
        modifier = selectedModifier.clip(shape = MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = { if (isSelectable) onItemLongClicked() else onItemClicked() },
                onLongClick = onItemLongClicked,
                //onDoubleClick = onItemLongClicked
            )
    ) {
        Card(modifier = Modifier.aspectRatio(1f)) {
            when {
                isSelected -> {
                    CheckCircle(isSelected = true)
                }

                isSelectable -> {
                    CheckCircle(isSelected = false)
                }
            }
        }
        Text(
            text = drawModel.name,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CheckCircle(modifier: Modifier = Modifier, isSelected: Boolean) {
    val icon = if (isSelected) Icons.Rounded.CheckCircle else Icons.Outlined.Circle
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(8.dp)
    )
}

@Composable
fun ClickableCheckCircle(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onIconClick: () -> Unit
) {
    IconButton(onClick = onIconClick, modifier = modifier.padding(8.dp)) {
        CheckCircle(modifier = modifier, isSelected = isSelected)
    }
}
