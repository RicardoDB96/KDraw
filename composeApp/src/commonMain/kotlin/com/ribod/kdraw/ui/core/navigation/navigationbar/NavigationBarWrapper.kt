package com.ribod.kdraw.ui.core.navigation.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ribod.kdraw.ui.core.navigation.Draw
import com.ribod.kdraw.ui.core.navigation.Routes
import com.ribod.kdraw.ui.main.tabs.home.HomeScreen

@Composable
fun NavigationBarWrapper(navController: NavHostController, mainNavController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {

        composable(route = Routes.Home.route) {
            HomeScreen(navigateToDraw = { drawId ->
                mainNavController.navigate(Draw(id = drawId))
            })
        }

        composable(route = Routes.Configuration.route) {
            Box(Modifier.fillMaxSize().background(Color.Blue))
        }
    }
}