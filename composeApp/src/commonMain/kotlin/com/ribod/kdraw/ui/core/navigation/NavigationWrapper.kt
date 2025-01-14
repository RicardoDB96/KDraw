package com.ribod.kdraw.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ribod.kdraw.ui.draw.DrawScreen
import com.ribod.kdraw.ui.main.MainScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationWrapper() {
    val mainNavController = rememberNavController()

    NavHost(navController = mainNavController, startDestination = Routes.Main.route) {
        composable(route = Routes.Main.route) {
            MainScreen(mainNavController)
        }

        composable<Draw> { navBackStackEntry ->
            val drawId = navBackStackEntry.toRoute<Draw>().id
            DrawScreen(
                onBackPressed = {
                    mainNavController.popBackStack(
                        route = Routes.Main.route,
                        inclusive = false
                    )
                },
                vm = koinViewModel(parameters = { parametersOf(drawId) })
            )
        }
    }
}