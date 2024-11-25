@file:Suppress("unused")

package com.pavlig43.roofapp.navigation

import com.pavlig43.roof_app.R

interface ScreensDestination {
    val title: Int
    val route: String
}

object AllMenuDestination {
    object TileLayout : ScreensDestination {
        override val title: Int = R.string.tile_layout
        override val route: String = "tile_layout"
    }

    object SaveDocuments : ScreensDestination {
        override val title: Int = R.string.save_documents
        override val route: String = "save_documents"
    }

    object ConstructorShape : ScreensDestination {
        override val title: Int = R.string.random_shape

        override val route: String = "random_shape"
    }

    fun getAllDestination() = arrayOf(TileLayout, ConstructorShape, SaveDocuments)
}

object PDFImageDestination : ScreensDestination {
    override val title: Int = 0
    override val route: String = "pdf_image"
    const val ARG_FILE_NAME = "file_name"
    val routeWithArgs: String = "$route/{$ARG_FILE_NAME}"
}
