package com.example.pdfcanvasdraw.abstractCanvas.drawShape

import android.graphics.PointF
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.abstractCanvas.сoordinateSystem.coordinateSystem
import com.example.pdfcanvasdraw.pdf.model.CountPxInOneCM

fun CanvasInterface.drawShapeWithRuler(
    shapeCanvas: ShapeCanvas,
    shapePaint: ShapePaint = defaultShapePaint,
    countPxInOneCM: CountPxInOneCM,
    startPointF: PointF = PointF(0F, 0F),


    ) {

    coordinateSystem(
        countCMInX = shapeCanvas.maxDistanceX.toInt(),
        countCMInY = shapeCanvas.maxDistanceY.toInt(),
        countPxInOneCM = countPxInOneCM,
        startPointFLine = startPointF

    )

    drawShape(
        shapeCanvas = shapeCanvas,
        countPxInOneCM = countPxInOneCM,
        startPointF = startPointF,
        shapePaint = shapePaint
    )
}
