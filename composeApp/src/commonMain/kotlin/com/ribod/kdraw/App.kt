package com.ribod.kdraw

import androidx.compose.runtime.Composable
import com.ribod.kdraw.ui.core.navigation.NavigationWrapper
import com.ribod.kdraw.ui.core.theme.KDrawTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KDrawTheme {
        NavigationWrapper()
    }
}