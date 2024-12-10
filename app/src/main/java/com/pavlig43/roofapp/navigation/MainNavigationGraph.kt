package com.pavlig43.roofapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pavlig43.roofapp.navigation.roofNavigation.RoofDestination
import com.pavlig43.roofapp.navigation.roofNavigation.roofNavigation

@Composable
fun MainNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RoofDestination.ConstructorShape.route,
    ) {
        roofNavigation(navController)
    }
}
