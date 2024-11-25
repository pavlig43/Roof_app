package com.example.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape

import android.graphics.PointF
import kotlin.math.absoluteValue

class ShapeOnCanvas(
    val listOfDots: List<PointF>,
    val nameValue: String = "",
) {
    val peakYMax = listOfDots.maxOfOrNull { it.y } ?: 0F
    val peakYMin = listOfDots.minOfOrNull { it.y } ?: 0F

    val peakXMax = listOfDots.maxOfOrNull { it.x } ?: 0F
    val peakXMin = listOfDots.minOfOrNull { it.x } ?: 0F
    val width = (peakYMax - peakYMin).absoluteValue
    val height = (peakXMax - peakXMin).absoluteValue

    fun copy(
        listOfDots: List<PointF> = this.listOfDots,
        nameValue: String = this.nameValue,
    ): ShapeOnCanvas {
        return ShapeOnCanvas(listOfDots, nameValue)
    }

    override fun toString(): String = listOfDots.toString()
}
