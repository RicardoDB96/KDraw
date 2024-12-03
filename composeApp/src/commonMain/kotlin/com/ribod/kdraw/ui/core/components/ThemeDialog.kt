package com.ribod.kdraw.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogState
import com.composables.core.Scrim

@Composable
fun ThemeDialog(
    modifier: Modifier = Modifier,
    state: DialogState,
    currentTheme: Int,
    onConfirmClick: (Int) -> Unit
) {
    Dialog(state = state) {
        val (selectedTheme, onThemeSelected) = remember { mutableStateOf(currentTheme) }
        Scrim()
        DialogPanel(
            modifier = modifier.displayCutoutPadding()
                .systemBarsPadding()
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(20.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Column {
                Column(modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)) {
                    Text(
                        text = "Choose theme",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(16.dp))

                    val themeOptions = listOf("System default", "Light", "Dark")

                    Column(Modifier.selectableGroup()) {
                        themeOptions.forEachIndexed { index, text ->
                            Row(
                                Modifier.fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (index == selectedTheme),
                                        onClick = { onThemeSelected(index) },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (index == selectedTheme),
                                    onClick = null // null for accessibility with screenreaders
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.End)
                ) {
                    TextButton(onClick = { state.visible = false }) {
                        Text(
                            "Cancel",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.padding(horizontal = 8.dp))
                    TextButton(onClick = {
                        onConfirmClick(selectedTheme)
                        state.visible = false
                    }) {
                        Text(
                            "OK",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}