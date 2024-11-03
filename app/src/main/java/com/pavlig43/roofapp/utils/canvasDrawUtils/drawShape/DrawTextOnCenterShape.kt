package com.pavlig43.roofapp.utils.canvasDrawUtils.drawShape

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import com.pavlig43.roofapp.TEXT_SIZE_SMALL
import com.pavlig43.roofapp.utils.canvasDrawUtils.ShapeCanvas
import com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem.RIGHT_DEGREE
import kotlin.math.cos
import kotlin.math.sin


fun Canvas.drawTextOnCenterShape(
    shapeCanvas: ShapeCanvas,
    rotateDegree: Float = RIGHT_DEGREE,
    drawTextParam: DrawTextParam = defaultDrawTextParam,
) {
    val paintText = TextPaint().apply {
        textSize = drawTextParam.textSize
        flags = defaultDrawTextParam.flags.fold(0) { acc, flag -> acc or flag }
    }

    /**
     *     длина текста в пикселях
     */
    val lenOfTextInPx = paintText.measureText(shapeCanvas.nameValue)

    /**
     * Смещение для середины тектса, используется, чтобы середина текста
     *   находилать посередине фигуры в зависимости от угла поворота
     */
    val offsetX = cos(Math.toRadians(rotateDegree.toDouble())) * lenOfTextInPx / 2
    val offsetY = sin(Math.toRadians(rotateDegree.toDouble())) * lenOfTextInPx / 2

    val x = shapeCanvas.peakXMin + shapeCanvas.maxDistanceX / 2 - offsetX
    val y = shapeCanvas.peakYMin + shapeCanvas.maxDistanceY / 2 - offsetY


    rotate(rotateDegree, x.toFloat(), y.toFloat())
    drawText(shapeCanvas.nameValue, x.toFloat(), y.toFloat(), paintText)
    rotate(-rotateDegree, x.toFloat(), y.toFloat())

}

data class DrawTextParam(
    val textSize: Float,
    val flags: List<Int>
)

private val defaultDrawTextParam = DrawTextParam(
    textSize = TEXT_SIZE_SMALL,
    flags = listOf(Paint.UNDERLINE_TEXT_FLAG, Paint.DITHER_FLAG)
)


