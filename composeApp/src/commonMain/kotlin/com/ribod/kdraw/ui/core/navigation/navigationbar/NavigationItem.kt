package com.ribod.kdraw.ui.core.navigation.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.ribod.kdraw.ui.core.navigation.Routes

sealed class NavigationItem {
    abstract val route: String
    abstract val title: String
    abstract val icon: @Composable () -> Unit

    data class Home(
        override val route: String = Routes.Home.route,
        override val title: String = "Home",
        override val icon: @Composable () -> Unit = {
            Icon(imageVector = Icons.Default.Home, contentDescription = title)
        },
    ) : NavigationItem()

    data class Configuration(
        override val route: String = Routes.Configuration.route,
        override val title: String = "Configuration",
        override val icon: @Composable () -> Unit = {
            Icon(imageVector = Icons.Default.Settings, contentDescription = title)
        },
    ) : NavigationItem()
}