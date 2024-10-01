package com.pavlig43.roofapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pavlig43.roofapp.ui.calculationTile4scat.CalculationTile4ScatMainScreen
import com.pavlig43.roofapp.ui.saveDocuments.ScreensSaveDocuments
import com.pavlig43.roofapp.ui.shapes.ShapesMainUi

interface ScreensDestination {
    val title: String
    val route: String

}

object TileLayout : ScreensDestination {
    override val title: String = "Расскладка черепицы"
    override val route: String = "tile_layout"
}

object SaveDocuments : ScreensDestination {
    override val title: String = "Сохраненные документы"
    override val route: String = "save_documents"
}

object Shapes : ScreensDestination {
    override val title: String = "Фигуры"
    override val route: String = "shapes"
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TileLayout.route,
    ) {
        composable(route = TileLayout.route) {
            CalculationTile4ScatMainScreen()
        }
        composable(route = SaveDocuments.route) {
            ScreensSaveDocuments()
        }
        composable(route = Shapes.route) {
            ShapesMainUi()
        }
    }
}
