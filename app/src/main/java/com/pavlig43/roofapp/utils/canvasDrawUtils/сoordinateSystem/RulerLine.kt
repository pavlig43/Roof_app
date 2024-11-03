package com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset

@Suppress("LongParameterList")
fun Canvas.rulerLine(
    startOffsetLine: Offset,
    endOffsetLine: Offset,
    countPxInOneCM: Float,
    countCM: Int,
    rulerParam: RulerParam,
    scaleRuler: Int,
    forXRuler: Boolean,

    ) {
    val countCeilOtherUnit = countCM.roundUpToNearestWithScale(scaleRuler)

    drawLine(
        startOffsetLine.x,
        startOffsetLine.y,
        endOffsetLine.x,
        endOffsetLine.y,
        Paint().apply {
            color = rulerParam.color
            strokeWidth = rulerParam.strokeWidth
            style = Paint.Style.STROKE
        }
    )



    for (i in 0..countCeilOtherUnit) {
        val offsetTick = if (forXRuler) {
            Offset(i * countPxInOneCM * scaleRuler + startOffsetLine.x, startOffsetLine.y)
        } else {
            Offset(startOffsetLine.x, i * countPxInOneCM * scaleRuler + startOffsetLine.y)
        }

        this.tickForRuler(
            numberOfTick = i,
            x = offsetTick.x,
            y = offsetTick.y,
            forXRuler = forXRuler
        )
    }

}
