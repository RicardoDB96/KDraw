package com.ribod.kdraw.ui.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationWrapper(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {

        composable(route = Routes.Home.route) {
            Box(Modifier.fillMaxSize().background(Color.Red))
        }

        composable(route = Routes.Configuration.route) {
            Box(Modifier.fillMaxSize().background(Color.Blue))
        }
    }
}