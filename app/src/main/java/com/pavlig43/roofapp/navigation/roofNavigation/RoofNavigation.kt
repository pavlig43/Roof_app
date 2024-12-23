package com.pavlig43.roofapp.navigation.roofNavigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pavlig43.roofapp.navigation.destination.PDFImageDestination
import com.pavlig43.roofapp.navigation.destination.PdfChangeDestination
import com.pavlig43.roofapp.navigation.destination.navigateToChangePdf
import com.pavlig43.roofapp.navigation.destination.navigateToResultPdf
import com.pavlig43.roofapp.ui.changeInfoText.TextInPdf
import com.pavlig43.roofapp.ui.pdfImage.ImagesFromPDF
import com.pavlig43.roofapp.ui.roof.calculationTile4scat.CalculationTile4ScatMainScreen
import com.pavlig43.roofapp.ui.roof.shapes.random.RandomShape
import com.pavlig43.roofapp.ui.saveDocuments.ScreensSaveDocuments

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
        RandomShape(moveToPdfResult = { path -> navController.navigateToResultPdf(path, true) })
    }
    composable(
        route = PDFImageDestination.routeWithArgs,
    ) {
        ImagesFromPDF(
            onBackNavigation = {
                navController.navigateUp()
            },
            onAdd = { navController.navigate(RoofDestination.ConstructorShape.route) },
            openChangeRoofInfo = { filePath ->
                navController.navigateToChangePdf(filePath)
            }

        )
    }
    composable(
        route = PdfChangeDestination.routeWithArgs,
    ) {
        TextInPdf(
            showResult = { path -> navController.navigateToResultPdf(path) },
            onBackNavigation = { navController.navigateUp() }
        )
    }
}
