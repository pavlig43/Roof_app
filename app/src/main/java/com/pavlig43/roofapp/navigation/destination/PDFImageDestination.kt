@file:Suppress("unused")

package com.pavlig43.roofapp.navigation.destination

import android.net.Uri
import androidx.navigation.NavHostController
import com.pavlig43.roofapp.FILE_NAME
import com.pavlig43.roofapp.IS_CONSTRUCTOR

object PDFImageDestination : ScreenDestination {
    override val title: Int = 0
    override val route: String = "pdf_image"
    private const val ARG_FILE_NAME = FILE_NAME
    private const val IS_CONTINUE_CONSTRUCTOR = IS_CONSTRUCTOR
    val routeWithArgs: String = "$route/{$ARG_FILE_NAME}/{$IS_CONTINUE_CONSTRUCTOR}"
}

fun NavHostController.navigateToResultPdf(filePAth: String, isConstructor: Boolean = false) =
    navigate(
        "${PDFImageDestination.route}/${Uri.encode(filePAth)}/$isConstructor",
    )
