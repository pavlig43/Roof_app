package com.pavlig43.roofapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pavlig43.roofapp.navigation.geometryNavigation.GeometryDestination
import com.pavlig43.roofapp.navigation.geometryNavigation.geometryNavigation
import com.pavlig43.roofapp.navigation.geometryNavigation.nested.geometryNestedDestination
import com.pavlig43.roofapp.navigation.roofNavigation.roofNavigation

@Composable
fun MainNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GeometryDestination.Flat.route,
    ) {
        roofNavigation(navController)
        geometryNavigation(navController)
        geometryNestedDestination()
    }
}
