package com.pavlig43.roofapp.navigation.geometryNavigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pavlig43.roofapp.navigation.ui.geometry.GeometryItems
import com.pavlig43.roofapp.navigation.ui.geometry.flatShapes

fun NavGraphBuilder.geometryNavigation(navController: NavHostController) {
    composable(route = GeometryDestination.Flat.route) {
        GeometryItems(
            listGeometryItemNav = flatShapes,
            onMove = { navController.navigate(it) }
        )
    }
}
