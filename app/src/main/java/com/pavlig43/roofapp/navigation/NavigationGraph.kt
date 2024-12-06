package com.pavlig43.roofapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pavlig43.roofapp.di.files.FileExtension
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
            CalculationTile4ScatMainScreen {
                navController.navigateToDefaultPdf()
            }
        }
        composable(route = AllMenuDestination.SaveDocuments.route) {
            ScreensSaveDocuments { fileName -> navController.navigate("${PDFImageDestination.route}/$fileName") }
        }

        composable(
            route = AllMenuDestination.ConstructorShape.route,

            ) {
            RandomShape { navController.navigateToDefaultPdf() }
        }
        composable(
            route = PDFImageDestination.routeWithArgs,
            arguments = listOf(navArgument(PDFImageDestination.ARG_FILE_NAME) { NavType.StringType })
        ) {
            ResultImagesFromPDF()
        }
    }
}

fun NavHostController.navigateToDefaultPdf() =
    navigate("${PDFImageDestination.route}/${FileExtension.PDF.defaultName}")
