package com.pavlig43.roofapp.navigation.geometryNavigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.geometry.GeneralGeometryShapeScreen
import com.pavlig43.roofapp.ui.geometry.flat.rightTriangle.RightTriangle
import com.pavlig43.roofapp.ui.geometry.flat.triangle.Triangle

fun NavGraphBuilder.geometryNestedDestination() {
    composable(GeometryNestedRoots.RIGHT_TRIANGLE) {
        GeneralGeometryShapeScreen(
            R.drawable.right_triangle
        ) { RightTriangle() }
    }
    composable(GeometryNestedRoots.TRIANGLE) {
        Triangle()
    }
}
