package com.pavlig43.roofapp.utils.canvasDrawUtils

import androidx.compose.ui.geometry.Offset
import kotlin.math.absoluteValue

class ShapeCanvas(
    val listOfDots: List<Offset>,
    val nameValue: String = "",
) {
    val peakYMax = listOfDots.maxOfOrNull { it.y } ?: 0F
    val peakYMin = listOfDots.minOfOrNull { it.y } ?: 0F

    val peakXMax = listOfDots.maxOfOrNull { it.x } ?: 0F
    val peakXMin = listOfDots.minOfOrNull { it.x } ?: 0F
    val maxDistanceY = peakYMax.absoluteValue + peakYMin.absoluteValue
    val maxDistanceX = peakXMax.absoluteValue + peakXMin.absoluteValue

    fun copy(
        listOfDots: List<Offset> = this.listOfDots,
        nameValue: String = this.nameValue
    ): ShapeCanvas {
        return ShapeCanvas(listOfDots, nameValue)
    }
}
