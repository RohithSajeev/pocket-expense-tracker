package com.rohith.pocket.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rohith.pocket.ui.screens.HistoryScreen
import com.rohith.pocket.ui.screens.HomeScreen
import com.rohith.pocket.ui.screens.InsightsScreen
import com.rohith.pocket.ui.screens.MonthlyScreen
import com.rohith.pocket.ui.screens.SettingsScreen
import com.rohith.pocket.ui.theme.PocketGreen
import com.rohith.pocket.viewmodel.TransactionViewModel

sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Destination("home", "Home", Icons.Default.Home)
    data object History : Destination("history", "History", Icons.Default.History)
    data object Insights : Destination("insights", "Insights", Icons.Default.Insights)
}

private val bottomTabs = listOf(Destination.Home, Destination.History, Destination.Insights)

@Composable
fun PocketNavGraph(viewModel: TransactionViewModel, isNotificationAccessGranted: Boolean) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination
            NavigationBar {
                bottomTabs.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = PocketGreen, selectedTextColor = PocketGreen)
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Destination.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onOpenSettings = { navController.navigate("settings") },
                    onSeeAllHistory = { navController.navigate(Destination.History.route) }
                )
            }
            composable(Destination.History.route) { HistoryScreen(viewModel) }
            composable(Destination.Insights.route) { InsightsScreen(viewModel) }
            composable("monthly") { MonthlyScreen(viewModel, onBack = { navController.popBackStack() }) }
            composable("settings") {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    isNotificationAccessGranted = isNotificationAccessGranted
                )
            }
        }
    }
}
