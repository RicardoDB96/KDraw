package com.ribod.kdraw.ui.main.tabs.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.composables.core.rememberDialogState
import com.ribod.kdraw.ui.core.components.ThemeDialog
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, vm: SettingsViewModel = koinViewModel()) {
    val state by vm.state.collectAsState()
    val themeDialogState = rememberDialogState(initiallyVisible = false)

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(modifier = modifier, title = { Text("Settings") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Button(onClick = {
                themeDialogState.visible = true
            }) {
                Text(
                    "Change theme: ${
                        when (state.currentTheme) {
                            0 -> "System"
                            1 -> "Light"
                            2 -> "Dark"
                            else -> "Unknown"
                        }
                    }"
                )
            }
        }
        ThemeDialog(
            state = themeDialogState,
            currentTheme = state.currentTheme,
            onConfirmClick = { theme -> vm.changeTheme(theme) }
        )
    }
}