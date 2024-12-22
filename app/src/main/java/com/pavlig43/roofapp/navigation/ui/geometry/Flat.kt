package com.pavlig43.roofapp.navigation.ui.geometry

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.navigation.geometryNavigation.nested.GeometryNestedRoots
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import kotlin.math.pow
import kotlin.math.sqrt

private val rightTriangleInCircle: (Float) -> List<Offset> = { radius ->
    val centerX = radius
    val centerY = radius

    val halfSize = sqrt(radius.pow(2) / 2)
    val x1 = centerX - halfSize
    val y1 = centerY + halfSize

    val x2 = centerX - halfSize
    val y2 = centerY - halfSize

    val x3 = centerX + halfSize
    val y3 = centerY + halfSize

    listOf(Offset(x1, y1), Offset(x2, y2), Offset(x3, y3))
}
private val rightTriangle = GeometryItemNav(
    name = R.string.right_triangle,
    route = GeometryNestedRoots.RIGHT_TRIANGLE,
    getShape = rightTriangleInCircle,
)
private val triangleInCircle: (Float) -> List<Offset> = { radius ->
    val centerX = radius
    val centerY = radius

    val halfSize = sqrt(radius.pow(2) / 2)
    val x1 = centerX - halfSize
    val y1 = centerY + halfSize

    val x2 = centerX
    val y2 = centerY - radius

    val x3 = centerX + halfSize
    val y3 = centerY + halfSize

    listOf(Offset(x1, y1), Offset(x2, y2), Offset(x3, y3))
}
private val triangle = GeometryItemNav(
    name = R.string.triangle,
    route = GeometryNestedRoots.TRIANGLE,
    getShape = triangleInCircle,
)
private val squareInCircle = { radius: Float ->
    val centerX = radius
    val centerY = radius
    val halfSize = sqrt(radius.pow(2) / 2)
    val x1 = centerX - halfSize
    val y1 = centerY + halfSize

    val x2 = centerX - halfSize
    val y2 = centerY - halfSize

    val x3 = centerX + halfSize
    val y3 = centerY - halfSize

    val x4 = centerX + halfSize
    val y4 = centerY + halfSize

    listOf(Offset(x1, y1), Offset(x2, y2), Offset(x3, y3), Offset(x4, y4))
}
private val square = GeometryItemNav(
    name = R.string.square,
    route = GeometryNestedRoots.SQUARE,
    getShape = squareInCircle,
)
val flatShapes = listOf(
    rightTriangle,
    triangle,
    square
)

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CircleButtonPreview() {
    Roof_appTheme {
        GeometryItems(
            listOf(
                rightTriangle,
                rightTriangle,
                rightTriangle,
                rightTriangle,
                rightTriangle,
            )
        )
    }
}
