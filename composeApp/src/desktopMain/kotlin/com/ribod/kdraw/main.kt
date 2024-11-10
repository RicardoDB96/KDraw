package com.ribod.kdraw

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ribod.kdraw.di.initKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KDraw",
    ) {
        initKoin()
        App()
    }
}