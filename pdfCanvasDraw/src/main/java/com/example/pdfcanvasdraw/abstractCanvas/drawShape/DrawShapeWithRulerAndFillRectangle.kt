package com.example.pdfcanvasdraw.abstractCanvas.drawShape

import android.graphics.PointF
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.pdf.model.CountPxInOneCM

@Suppress("LongParameterList")
fun CanvasInterface.drawShapeWithRulerAndFillRectangle(
    shapeCanvas: ShapeCanvas,
    shapePaint: ShapePaint = defaultShapePaint,
    countPxInOneCM: CountPxInOneCM,
    startPointF: PointF = PointF(0F, 0F),
    listOfRectangle: List<ShapeCanvas>,
    rectanglePaint: ShapePaint = thinShapePaint,
) {

    drawShapeWithRuler(
        shapeCanvas = shapeCanvas,
        startPointF = startPointF,
        countPxInOneCM = countPxInOneCM,
        shapePaint = shapePaint
    )


    listOfRectangle.forEach { rightRectangle ->
        drawShape(
            shapeCanvas = rightRectangle,
            countPxInOneCM = countPxInOneCM,
            startPointF = startPointF,
            shapePaint = rectanglePaint

        )
    }
}
