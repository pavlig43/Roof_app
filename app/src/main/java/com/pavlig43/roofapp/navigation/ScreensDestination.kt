package com.pavlig43.roofapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.calculationTile4scat.CalculationTile4ScatMainScreen
import com.pavlig43.roofapp.ui.saveDocuments.ScreensSaveDocuments
import com.pavlig43.roofapp.ui.shapes.ShapesMainUi

interface ScreensDestination {
    val title: Int
    val route: String
}

object AllDestination {
    object TileLayout : ScreensDestination {
        override val title: Int = R.string.tile_layout
        override val route: String = "tile_layout"
    }

    object SaveDocuments : ScreensDestination {
        override val title: Int = R.string.save_documents
        override val route: String = "save_documents"
    }

    object Shapes : ScreensDestination {
        override val title: Int = R.string.shapes
        override val route: String = "shapes"
    }

    fun getAllDestination() = arrayOf(TileLayout, SaveDocuments, Shapes)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AllDestination.TileLayout.route,
    ) {
        composable(route = AllDestination.TileLayout.route) {
            CalculationTile4ScatMainScreen()
        }
        composable(route = AllDestination.SaveDocuments.route) {
            ScreensSaveDocuments()
        }
        composable(route = AllDestination.Shapes.route) {
            ShapesMainUi()
        }
    }
}
