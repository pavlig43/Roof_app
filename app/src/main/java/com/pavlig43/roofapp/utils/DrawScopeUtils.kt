package com.pavlig43.roofapp.utils

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.pavlig43.roofapp.model.Dot

/**
 * Рисует на канвасе экрана устройства точку фигуры
 */
@Suppress("LongParameterList")
fun DrawScope.drawDot(
    downOffset: Boolean,
    offsetY: Float = 35f,
    startDot: Boolean = false,
    center: Offset,
    dot: Dot,
    textPaint: Paint =
        Paint().apply {
            textSize = 20f //
            color = Color.Black.toArgb()
            textAlign = Paint.Align.CENTER
        },
) {
    drawCircle(if (startDot) Color.Green else Color.Black, center = center, radius = 15f)
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "(${dot.offset.x}cm, ${dot.offset.y}cm)",
            center.x,
            center.y + if (!downOffset) offsetY else -offsetY,
            textPaint,
        )
    }
}

fun DrawScope.drawRuler(
    screenWidth: Float,
    screenHeight: Float,
    textPaint: Paint =
        Paint().apply {
            textSize = 60f //
            color = Color.Black.toArgb()
            textAlign = Paint.Align.CENTER
        },
) {
    drawLine(
        Color.Black,
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.8).toFloat()),
        strokeWidth = 10f,
    )
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "X",
            screenWidth * 0.83f,
            screenHeight * 0.05f,
            textPaint,
        )
        drawText(
            "Y",
            screenWidth * 0.05f,
            screenHeight * 0.83f,
            textPaint,
        )
    }

    drawLine(
        Color.Black,
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
        Offset((screenWidth * 0.8).toFloat(), (screenHeight * 0.05).toFloat()),
        strokeWidth = 10f,
    )
}
