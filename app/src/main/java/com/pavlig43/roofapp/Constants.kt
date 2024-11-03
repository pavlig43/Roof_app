package com.pavlig43.roofapp

import androidx.compose.ui.geometry.Offset
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.roofapp.utils.canvasDrawUtils.CountPxInOneCM
import com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem.roundUpToNearestToFullScale

const val A4X = 595 // Ширина A4 в пикселях
const val A4Y = 841 // Высота A4 в пикселях
const val PADDING_PERCENT = 0.05F
const val CM_IN_ONE_METER = 100

data class PageConfig(
    val x: Int = A4X,
    val y: Int = A4Y,
    val paddingPercentX: Float = PADDING_PERCENT,
    val paddingPercentY: Float = PADDING_PERCENT,
) {
    val startOffset = Offset(x * paddingPercentX, y * paddingPercentY)
}

fun PageConfig.getCountPxInOneCM(
    coordinateShape: CoordinateShape,
): CountPxInOneCM {

    fun getCountPxInOneCM(
        pageSideSize: Int,
        pagePaddingPercent: Float,
        maxDistance: Int,
    ): Float =
        (pageSideSize * (1 - 2 * pagePaddingPercent)) / maxDistance.roundUpToNearestToFullScale()
    return CountPxInOneCM(
        x = getCountPxInOneCM(x, paddingPercentX, coordinateShape.maxDistanceX.toInt()),
        y = getCountPxInOneCM(y, paddingPercentY, coordinateShape.maxDistanceY.toInt())
    )
}
