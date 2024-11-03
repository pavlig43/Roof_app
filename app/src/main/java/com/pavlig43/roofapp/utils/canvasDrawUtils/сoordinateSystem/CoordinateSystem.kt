package com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem

import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.CM_IN_ONE_METER
import com.pavlig43.roofapp.utils.canvasDrawUtils.CountPxInOneCM
import kotlin.math.ceil

const val RIGHT_DEGREE = 90F
private const val DEFAULT_STROKE_WIDTH_RULER = 4f

data class RulerParam(
    val strokeWidth: Float = DEFAULT_STROKE_WIDTH_RULER,
    val color: Int = Color.BLACK,
)

@Suppress("LongParameterList")
fun Canvas.coordinateSystem(
    countPxInOneCM: CountPxInOneCM,
    countCMInX: Int,
    countCMInY: Int,

    startOffsetLine: Offset = Offset(0f, 0f),
    rulerParam: RulerParam = RulerParam(),
    scaleRuler: Int = CM_IN_ONE_METER,
) {
    val endOffsetLineX =
        Offset(countCMInX * countPxInOneCM.x * scaleRuler + startOffsetLine.x, startOffsetLine.y)

    val endOffsetLineY =
        Offset(startOffsetLine.x, countCMInY * countPxInOneCM.y + startOffsetLine.y)

    rulerLine(
        startOffsetLine = startOffsetLine,
        endOffsetLine = endOffsetLineX,

        countPxInOneCM = countPxInOneCM.x,
        countCM = countCMInX,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = true,


        )
    rulerLine(
        startOffsetLine = startOffsetLine,
        endOffsetLine = endOffsetLineY,
        countPxInOneCM = countPxInOneCM.y,
        countCM = countCMInY,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = false,
    )
}

/**
 * 655.roundUpToNearestToFullScale(100) -> 7m
 */
fun Int.roundUpToNearestToFullScale(scale: Int = CM_IN_ONE_METER): Int {

    return (ceil(this.toDouble() / scale) * scale).toInt()
}

/**
 * 655.roundUpToNearestWithScale(100) -> 700
 */
fun Int.roundUpToNearestWithScale(scale: Int = CM_IN_ONE_METER): Int {

    return ceil(this.toDouble() / scale).toInt()
}

