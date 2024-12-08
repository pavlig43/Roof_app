package com.pavlig43.roofapp.navigation.roofNavigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pavlig43.roofapp.navigation.destination.PDFImageDestination
import com.pavlig43.roofapp.navigation.destination.navigateToResultPdf
import com.pavlig43.roofapp.ui.calculationTile4scat.CalculationTile4ScatMainScreen
import com.pavlig43.roofapp.ui.pdfImage.ResultImagesFromPDF
import com.pavlig43.roofapp.ui.saveDocuments.ScreensSaveDocuments
import com.pavlig43.roofapp.ui.shapes.random.RandomShape

fun NavGraphBuilder.roofNavigation(navController: NavHostController) {
    composable(route = RoofDestination.TileLayout.route) {
        CalculationTile4ScatMainScreen { path ->
            navController.navigateToResultPdf(path)
        }
    }
    composable(route = RoofDestination.SaveDocuments.route) {
        ScreensSaveDocuments { path -> navController.navigateToResultPdf(path) }
    }

    composable(
        route = RoofDestination.ConstructorShape.route,
    ) {
        RandomShape { path -> navController.navigateToResultPdf(path) }
    }
    composable(
        route = PDFImageDestination.routeWithArgs,
        arguments = listOf(navArgument(PDFImageDestination.ARG_FILE_NAME) { NavType.StringType })
    ) {
        ResultImagesFromPDF()
    }
}
