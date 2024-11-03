package com.pavlig43.roofapp.utils.canvasDrawUtils.drawShape

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.FULL_ALPHA
import com.pavlig43.roofapp.SEMI_ALPHA
import com.pavlig43.roofapp.STROKE_WIDTH_MEDIUM_PDF_CANVAS
import com.pavlig43.roofapp.STROKE_WIDTH_THIN_PDF_CANVAS
import com.pavlig43.roofapp.utils.canvasDrawUtils.CountPxInOneCM
import com.pavlig43.roofapp.utils.canvasDrawUtils.ShapeCanvas
import com.pavlig43.roofapp.utils.zipWithNextCircular


fun Canvas.drawShape(
    shapeCanvas: ShapeCanvas,
    countPxInOneCM: CountPxInOneCM,
    startOffset: Offset = Offset(0F, 0F),
    shapePaint: ShapePaint = defaultShapePaint,

    ) {
    Log.d("startOffset", startOffset.toString())
    val paint: Paint =
        Paint().apply {
            color = shapePaint.color
            strokeWidth = shapePaint.strokeWidth
            alpha = shapePaint.strokeAlpha
            style = Paint.Style.STROKE
        }


    val offsetListOfDots = shapeCanvas.listOfDots.map { offset ->
        Offset(
            x = offset.x * countPxInOneCM.x + startOffset.x,
            y = offset.y * countPxInOneCM.y + startOffset.y,
        )
    }
    val offsetShapeCanvas = shapeCanvas.copy(offsetListOfDots)

    val path = android.graphics.Path().apply {
        offsetShapeCanvas.listOfDots.zipWithNextCircular { first, second ->
            moveTo(first.x, first.y)
            lineTo(second.x, second.y)
        }
    }

    drawPath(path, paint)
    if (offsetShapeCanvas.nameValue.isNotBlank()) {
        drawTextOnCenterShape(
            offsetShapeCanvas,
        )
    }


}

data class ShapePaint(
    val strokeWidth: Float = STROKE_WIDTH_MEDIUM_PDF_CANVAS,
    val strokeAlpha: Int = SEMI_ALPHA,
    val color: Int = Color.BLACK,
)

internal val defaultShapePaint = ShapePaint()
internal val thinShapePaint = ShapePaint(
    strokeWidth = STROKE_WIDTH_THIN_PDF_CANVAS,
    strokeAlpha = FULL_ALPHA,
    color = Color.RED
)
