package com.pavlig43.roofapp.utils.canvasDrawUtils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH
import com.pavlig43.roofapp.STROKE_WIDTH_MEDIUM_PDF_CANVAS

@Suppress("LongParameterList")
fun rulerOnCanvasPDF(
    canvas: Canvas,
    countCeilCMWidth: Int,
    countCeilCMHeight: Int,
    oneCMInHeightYtPx: Float,
    oneCMInWidthXPx: Float,
    paint: Paint =
        Paint().apply {
            color = Color.BLACK
            strokeWidth = STROKE_WIDTH_MEDIUM_PDF_CANVAS
            style = Paint.Style.STROKE
        },
    widthPage: Int = A4WIDTH,
    heightPage: Int = A4HEIGHT,
    paddingWidth: Float,
    paddingHeight: Float,
) {
    horizontalRuler(
        canvas = canvas,
        oneCMInHeightYtPx = oneCMInHeightYtPx,
        countCeilCMWidth = countCeilCMWidth,
        paint,
        paddingWidth,
        paddingHeight,
    )

    verticalRuler(
        canvas = canvas,
        oneCMInWidthXPx = oneCMInWidthXPx,
        countCeilCMHeight = countCeilCMHeight,
        paint,
        widthPage,
        heightPage,
        paddingWidth,
        paddingHeight,
    )
}

@Suppress("LongParameterList")
fun horizontalRuler(
    canvas: Canvas,
    oneCMInHeightYtPx: Float,
    countCeilCMWidth: Int,
    paint: Paint,
    paddingWidth: Float,
    paddingHeight: Float,
) {
    canvas.drawLine(
        paddingWidth,
        paddingHeight,
        paddingWidth,
        countCeilCMWidth * oneCMInHeightYtPx + paddingHeight,
        paint,
    )
    for (m in 0..countCeilCMWidth) {
        val y =
            m.toFloat() * oneCMInHeightYtPx * 100 + paddingHeight
        canvas.drawLine(
            paddingWidth - 10,
            y,
            paddingWidth + 10,
            y,
            Paint().apply { strokeWidth = 3f },
        )

        canvas.rotate(90f, paddingWidth - 25, y - 10)

        canvas.drawText(
            "$m м",
            paddingWidth - 25,
            y - 10,
            Paint().apply { textSize = 20f },
        )

        canvas.rotate(-90f, paddingWidth - 25, y - 10)
    }
}

@Suppress("LongParameterList")
fun verticalRuler(
    canvas: Canvas,
    oneCMInWidthXPx: Float,
    countCeilCMHeight: Int,
    paint: Paint =
        Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f // Толщина линии
            style = Paint.Style.STROKE
        },
    widthPage: Int = A4WIDTH,
    heightPage: Int = A4HEIGHT,
    paddingWidth: Float = (widthPage * 0.05).toFloat(),
    paddingHeight: Float = (heightPage * 0.05).toFloat(),
) {
    canvas.drawLine(
        paddingWidth,
        paddingHeight,
        countCeilCMHeight * oneCMInWidthXPx + paddingWidth,
        paddingHeight,
        paint,
    )

    for (i in 0..countCeilCMHeight) {
        val x = i.toFloat() * oneCMInWidthXPx * 100 + paddingWidth
        canvas.drawLine(
            x,
            paddingHeight - 10,
            x,
            paddingHeight + 10,
            Paint().apply { strokeWidth = 3f },
        )

        canvas.drawText(
            "$i м",
            x - 10,
            paddingHeight - 25,
            Paint().apply { textSize = 20f },
        )
    }
}
