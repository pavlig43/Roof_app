package com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem

import android.graphics.Canvas
import android.graphics.Paint

private const val HALF_SIZE_TICK = 10F
private const val PADDING_TEXT_TICK = 25F
private const val BORDER_STROKE_TICK = 3F
private const val TEXT_SIZE_TICK = 20F

data class TickParam(
    val halfSize: Float = HALF_SIZE_TICK,
    val paddingText: Float = PADDING_TEXT_TICK,
    val borderStroke: Float = BORDER_STROKE_TICK,
    val textSize: Float = TEXT_SIZE_TICK
)

fun Canvas.tickForRuler(
    numberOfTick: Int,
    x: Float,
    y: Float,
    tickParam: TickParam = TickParam(),
    forXRuler: Boolean,

    ) {
    if (forXRuler) {
        drawLine(
            x,
            y - tickParam.halfSize,
            x,
            y + tickParam.halfSize,
            Paint().apply { strokeWidth = tickParam.borderStroke },
        )

        drawText(
            "$numberOfTick",
            x,
            y - tickParam.paddingText,
            Paint().apply { textSize = tickParam.textSize },
        )
    } else {
        drawLine(
            x - tickParam.halfSize,
            y,
            x + tickParam.halfSize,
            y,
            Paint().apply { strokeWidth = tickParam.borderStroke },
        )
        rotate(RIGHT_DEGREE, x - tickParam.paddingText, y)
        drawText(
            "$numberOfTick",
            x - tickParam.paddingText,
            y,
            Paint().apply { textSize = tickParam.textSize },
        )
        rotate(-RIGHT_DEGREE, x - tickParam.paddingText, y)
    }

}
