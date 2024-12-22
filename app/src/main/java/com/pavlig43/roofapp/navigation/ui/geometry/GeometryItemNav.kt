package com.pavlig43.roofapp.navigation.ui.geometry

import androidx.annotation.StringRes
import androidx.compose.ui.geometry.Offset

data class GeometryItemNav(
    @StringRes val name: Int,
    val route: String,
    val getShape: Float.() -> List<Offset>,
)
