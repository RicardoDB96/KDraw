package com.ribod.kdraw.ui.core.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogState
import com.composables.core.Scrim

@Composable
fun DeleteDialog(modifier: Modifier = Modifier, state: DialogState, onDeleteClick: () -> Unit) {
    Dialog(state = state) {
        Scrim()
        DialogPanel(
            modifier = modifier
                .displayCutoutPadding()
                .systemBarsPadding()
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(20.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            enter = scaleIn(initialScale = 0.8f) + fadeIn(tween(durationMillis = 250)),
            exit = scaleOut(targetScale = 0.8f) + fadeOut(tween(durationMillis = 250)),
        ) {
            Column {
                Column(modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)) {
                    Text(
                        text = "Delete selected draws?",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "This action cannot be undone.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.End)
                ) {
                    TextButton(onClick = { state.visible = false }) {
                        Text("Cancel", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.padding(horizontal = 8.dp))
                    TextButton(onClick = {
                        onDeleteClick()
                        state.visible = false
                    }) {
                        Text("Delete", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

        }
    }
}