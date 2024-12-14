package com.pavlig43.roofapp.navigation.destination

import android.net.Uri
import androidx.navigation.NavHostController
import com.pavlig43.roofapp.FILE_NAME

object PdfChangeDestination : ScreenDestination {
    override val title: Int = 0
    override val route: String = "change_pdf"
    private const val ARG_FILE_NAME = FILE_NAME
    val routeWithArgs: String = "$route/{$ARG_FILE_NAME}"
}

fun NavHostController.navigateToChangePdf(filePAth: String) =
    navigate("${PdfChangeDestination.route}/${Uri.encode(filePAth)}")
