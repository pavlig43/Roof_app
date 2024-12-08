@file:Suppress("unused")

package com.pavlig43.roofapp.navigation.destination

import android.net.Uri
import androidx.navigation.NavHostController

object PDFImageDestination : ScreenDestination {
    override val title: Int = 0
    override val route: String = "pdf_image"
    const val ARG_FILE_NAME = "file_name"
    val routeWithArgs: String = "$route/{$ARG_FILE_NAME}"
}

fun NavHostController.navigateToResultPdf(filePAth: String) =
    navigate("${PDFImageDestination.route}/${Uri.encode(filePAth)}")
