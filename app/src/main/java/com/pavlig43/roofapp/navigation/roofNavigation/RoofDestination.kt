package com.pavlig43.roofapp.navigation.roofNavigation

import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.navigation.destination.ScreenDestination

object RoofDestination {
    object TileLayout : ScreenDestination {
        override val title: Int = R.string.tile_layout
        override val route: String = "tile_layout"
    }

    object SaveDocuments : ScreenDestination {
        override val title: Int = R.string.save_documents
        override val route: String = "save_documents"
    }

    object ConstructorShape : ScreenDestination {
        override val title: Int = R.string.random_shape

        override val route: String = "random_shape"
    }

    fun getAllDestination() = arrayOf(TileLayout, ConstructorShape, SaveDocuments)
}
