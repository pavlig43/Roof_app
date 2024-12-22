package com.pavlig43.roofapp.navigation.geometryNavigation

import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.navigation.destination.ScreenDestination

object GeometryDestination {
    object Flat : ScreenDestination {
        override val title: Int = R.string.flat_shape
        override val route: String = "flat_shape"
    }

    fun getAllDestination(): Array<ScreenDestination> = arrayOf(Flat)
}
