package com.ribod.kdraw.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object Main: Routes("main")

    // Navigation Bar
    @Serializable
    data object Home: Routes("home")

    @Serializable
    data object Configuration: Routes("config")
}

@Serializable
data class Draw(val id: Long)