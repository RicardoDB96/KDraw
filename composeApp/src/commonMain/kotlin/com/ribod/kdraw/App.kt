package com.ribod.kdraw

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ribod.kdraw.ui.core.navigation.NavigationItem
import com.ribod.kdraw.ui.core.navigation.NavigationWrapper
import com.ribod.kdraw.ui.core.theme.KDrawTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KDrawTheme {
        val items = listOf(NavigationItem.Home(), NavigationItem.Configuration())

        val mainNavController = rememberNavController()
        val navBackStackEntry by mainNavController.currentBackStackEntryAsState()

        NavigationSuiteScaffold(navigationSuiteItems = {
            navigationBar(
                items = items,
                navController = mainNavController,
                navBackStackEntry = navBackStackEntry
            )
        }) {
            NavigationWrapper(navController = mainNavController)
        }
    }
}

fun NavigationSuiteScope.navigationBar(
    items: List<NavigationItem>,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    val currentDestination = navBackStackEntry?.destination

    items.forEach { item ->
        item(
            icon = item.icon,
            label = { Text(item.title) },
            onClick = {
                navController.navigate(route = item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
        )
    }
}