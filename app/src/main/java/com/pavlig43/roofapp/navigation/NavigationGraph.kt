package com.pavlig43.roofapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pavlig43.roofapp.ui.calculationTile4scat.CalculationTile4ScatMainScreen
import com.pavlig43.roofapp.ui.pdfImage.ResultImagesFromPDF
import com.pavlig43.roofapp.ui.saveDocuments.ScreensSaveDocuments
import com.pavlig43.roofapp.ui.shapes.random.RandomShape

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AllMenuDestination.TileLayout.route,
    ) {
        composable(route = AllMenuDestination.TileLayout.route) {
            CalculationTile4ScatMainScreen { path ->
                navController.navigateToResultPdf(path)
            }
        }
        composable(route = AllMenuDestination.SaveDocuments.route) {
            ScreensSaveDocuments { path -> navController.navigateToResultPdf(path) }
        }

        composable(
            route = AllMenuDestination.ConstructorShape.route,

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
}

private fun NavHostController.navigateToResultPdf(filePAth: String) =
    navigate("${PDFImageDestination.route}/${Uri.encode(filePAth)}")
