package com.pavlig43.roof_app.utils

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.text.drawText
import com.pavlig43.roof_app.model.Dot


/**
 * Рисует на канвасе экрана устройства точку фигуры
 */
fun DrawScope.drawDot(
    downOffset: Boolean,
    offsetY: Float = 35f,
    startDot: Boolean = false,
    center: Offset,
    dot: Dot,
    textPaint: Paint = Paint().apply {
        textSize = 20f //
        color = Color.Black.toArgb()
        textAlign = Paint.Align.CENTER
    }
) {
    drawCircle(if (startDot) Color.Green else Color.Black, center = center, radius = 15f)
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "(${dot.distanceX.toInt()}cm, ${dot.distanceY.toInt()}cm)",
            center.x,
            center.y + if (!downOffset) offsetY else -offsetY,
            textPaint
        )
    }
}
fun DrawScope.drawRuler(
    screenWidth:Float,
    screenHeight:Float,
    textPaint: Paint = Paint().apply {
        textSize = 60f //
        color = Color.Black.toArgb()
        textAlign = Paint.Align.CENTER
    }
){
    drawLine(
        Color.Black,
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.8).toFloat()),
        strokeWidth = 10f
    )
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "X",
            screenWidth*0.83f,
            screenHeight*0.05f,
            textPaint
            )
        drawText(
            "Y",
            screenWidth*0.05f,
            screenHeight*0.83f,
            textPaint
        )
    }

    drawLine(
        Color.Black,
        Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
        Offset((screenWidth * 0.8).toFloat(), (screenHeight * 0.05).toFloat()),
        strokeWidth = 10f
    )
}