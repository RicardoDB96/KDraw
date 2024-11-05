package com.ribod.kdraw.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object Home: Routes("home")

    @Serializable
    data object Configuration: Routes("config")
}