package com.ribod.kdraw.ui.core.navigation.navigationbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ribod.kdraw.ui.core.navigation.Draw
import com.ribod.kdraw.ui.core.navigation.Routes
import com.ribod.kdraw.ui.main.tabs.home.HomeScreen
import com.ribod.kdraw.ui.main.tabs.settings.SettingsScreen

@Composable
fun NavigationBarWrapper(navController: NavHostController, mainNavController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {

        composable(route = Routes.Home.route) {
            HomeScreen(navigateToDraw = { drawId ->
                mainNavController.navigate(Draw(id = drawId))
            })
        }

        composable(route = Routes.Configuration.route) {
            SettingsScreen()
        }
    }
}