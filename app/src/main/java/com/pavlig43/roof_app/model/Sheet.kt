package com.pavlig43.roof_app.model

import androidx.compose.ui.geometry.Offset


data class Sheet(
    val profile: RoofMetal = RoofMetal.TILE,
     val widthGeneral: Double = 1.18,
    val overlap:Double = 0.08,
    val visible:Double = widthGeneral-overlap,
    val multiplicity:Double = 0.05
    )



enum class RoofMetal {
    TILE,
    PROFILE
}
