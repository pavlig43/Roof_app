package com.pavlig43.roofapp.utils.canvasDrawUtils.drawShape

import android.graphics.Canvas
import androidx.compose.ui.geometry.Offset

import com.pavlig43.roofapp.utils.canvasDrawUtils.CountPxInOneCM
import com.pavlig43.roofapp.utils.canvasDrawUtils.ShapeCanvas

fun Canvas.drawShapeWithRulerAndFillRectangle(
    shapeCanvas: ShapeCanvas,
    countPxInOneCM: CountPxInOneCM,
    rectanglePaint: ShapePaint = thinShapePaint,
    listOfRectangle: List<ShapeCanvas>,
    startOffset: Offset = Offset(0F, 0F),
) {
    drawShapeWithRuler(
        shapeCanvas = shapeCanvas,
        countPxInOneCM = countPxInOneCM,
        startOffset = startOffset
    )

    listOfRectangle.forEach { rightRectangle ->
        drawShape(
            rightRectangle,
            countPxInOneCM = countPxInOneCM,
            startOffset = startOffset,
            shapePaint = rectanglePaint

        )
    }
}
