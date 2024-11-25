package com.example.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem

import com.example.pdfcanvasdraw.DEFAULT_COLOR
import com.example.pdfcanvasdraw.RIGHT_DEGREE
import com.example.pdfcanvasdraw.STROKE_WIDTH_MEDIUM_PLUS
import com.example.pdfcanvasdraw.TEXT_SIZE_MEDIUM_PLUS
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface

private const val HALF_SIZE_TICK = 10F
private const val PADDING_TEXT_TICK = 25F

data class TickParam(
    val halfSize: Float = HALF_SIZE_TICK,
    val paddingText: Float = PADDING_TEXT_TICK,
    val borderStroke: Float = STROKE_WIDTH_MEDIUM_PLUS,
    val tickColor: Int = DEFAULT_COLOR,
    val textSize: Float = TEXT_SIZE_MEDIUM_PLUS,
    val textColor: Int = DEFAULT_COLOR,
)

fun CanvasInterface.tickForRuler(
    numberOfTick: Int,
    x: Float,
    y: Float,
    tickParam: TickParam = TickParam(),
    forXRuler: Boolean,
) {
    val paintTick =
        createPaint().apply {
            color = tickParam.tickColor
            strokeWidth = tickParam.borderStroke
        }
    val paintText =
        createPaintText().apply {
            textColor = tickParam.textColor
            textSize = tickParam.textSize
        }

    if (forXRuler) {
        drawLine(
            x,
            y - tickParam.halfSize,
            x,
            y + tickParam.halfSize,
            paintTick,
        )

        drawAndRotateText(
            pivotX = x,
            pivotY = y - tickParam.paddingText,
            text = "$numberOfTick",
            paintText = paintText,
        )
    } else {
        drawLine(
            x - tickParam.halfSize,
            y,
            x + tickParam.halfSize,
            y,
            paintTick,
        )
        drawAndRotateText(
            degree = RIGHT_DEGREE,
            pivotX = x - tickParam.paddingText,
            pivotY = y,
            text = "$numberOfTick",
            paintText = paintText,
        )
    }
}