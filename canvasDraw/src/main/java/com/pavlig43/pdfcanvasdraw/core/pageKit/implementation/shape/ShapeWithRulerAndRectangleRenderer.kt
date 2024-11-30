package com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.shape

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.ShapeOnCanvas
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.drawShapeWithRulerAndFillRectangle
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

class ShapeWithRulerAndRectangleRenderer(
    override val pageConfig: PageConfig = PageConfig(),
    private val shapeOnCanvas: ShapeOnCanvas,
    private val listOfRectangle: List<ShapeOnCanvas>,
) : PageRenderer() {
    override suspend fun CanvasInterface.drawContent() {
        val countPxInOneCM =
            countPxInOneCM(shapeOnCanvas.peakXMax.toInt(), shapeOnCanvas.peakYMax.toInt())

        drawShapeWithRulerAndFillRectangle(
            shapeOnCanvas = shapeOnCanvas,
            countPxInOneCM = countPxInOneCM,
            listOfRectangle = listOfRectangle,
            startPointF = pageConfig.startPointF,
        )
    }

    override fun handleGetTap(tapPointF: PointF) {
        TODO("Not yet implemented")
    }
}
