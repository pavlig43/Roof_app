package com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.RulerParam
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.TickParam
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.coordinateSystem
import com.pavlig43.pdfcanvasdraw.core.metrics.CountPxInOneCM

@Suppress("LongParameterList")
fun CanvasInterface.drawShapeWithRuler(
    shapeOnCanvas: ShapeOnCanvas,
    shapePaint: ShapePaint = defaultShapePaint,
    countPxInOneCM: CountPxInOneCM,
    startPointF: PointF = PointF(0F, 0F),
    rulerParam: RulerParam = RulerParam(),
    tickParam: TickParam = TickParam(),
) {
    coordinateSystem(
        countCMInX = shapeOnCanvas.height.toInt(),
        countCMInY = shapeOnCanvas.width.toInt(),
        countPxInOneCM = countPxInOneCM,
        startPointFLine = startPointF,
        rulerParam = rulerParam,
        tickParam = tickParam,
    )

    drawShape(
        shapeOnCanvas = shapeOnCanvas,
        countPxInOneCM = countPxInOneCM,
        startPointF = startPointF,
        shapePaint = shapePaint,
    )
}
