package com.example.pdfcanvasdraw.pdf.page.implementation.shape

import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.abstractCanvas.drawShape.ShapeCanvas
import com.example.pdfcanvasdraw.abstractCanvas.drawShape.drawShapeWithRulerAndFillRectangle
import com.example.pdfcanvasdraw.pdf.model.PageConfig
import com.example.pdfcanvasdraw.pdf.page.PageRenderer

class ShapeWithRulerAndRectangleRenderer(
    override val pageConfig: PageConfig = PageConfig(),
    private val shapeCanvas: ShapeCanvas,
    private val listOfRectangle: List<ShapeCanvas>
) : PageRenderer() {

    override fun CanvasInterface.drawContent() {
        val countPxInOneCM = pageConfig.getCountPxInOneCM(shapeCanvas)


        drawShapeWithRulerAndFillRectangle(
            shapeCanvas = shapeCanvas,
            countPxInOneCM = countPxInOneCM,
            listOfRectangle = listOfRectangle,
            startPointF = pageConfig.startPointF
        )
    }
}

