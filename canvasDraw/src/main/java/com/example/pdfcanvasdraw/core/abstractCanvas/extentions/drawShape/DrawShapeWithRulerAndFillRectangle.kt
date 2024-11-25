package com.example.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape

import android.graphics.PointF
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.core.metrics.CountPxInOneCM

@Suppress("LongParameterList")
fun CanvasInterface.drawShapeWithRulerAndFillRectangle(
    shapeOnCanvas: ShapeOnCanvas,
    shapePaint: ShapePaint = defaultShapePaint,
    countPxInOneCM: CountPxInOneCM,
    startPointF: PointF = PointF(0F, 0F),
    listOfRectangle: List<ShapeOnCanvas>,
    rectanglePaint: ShapePaint = thinShapePaint,
) {
    drawShapeWithRuler(
        shapeOnCanvas = shapeOnCanvas,
        startPointF = startPointF,
        countPxInOneCM = countPxInOneCM,
        shapePaint = shapePaint,
    )

    listOfRectangle.forEach { rightRectangle ->
        drawShape(
            shapeOnCanvas = rightRectangle,
            countPxInOneCM = countPxInOneCM,
            startPointF = startPointF,
            shapePaint = rectanglePaint,
        )
    }
}
