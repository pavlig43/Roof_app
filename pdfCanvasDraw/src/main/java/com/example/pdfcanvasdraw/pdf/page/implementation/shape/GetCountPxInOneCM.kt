package com.example.pdfcanvasdraw.pdf.page.implementation.shape

import com.example.pdfcanvasdraw.abstractCanvas.drawShape.ShapeCanvas
import com.example.pdfcanvasdraw.extensions.roundUpToNearestToFullScale
import com.example.pdfcanvasdraw.pdf.model.CountPxInOneCM
import com.example.pdfcanvasdraw.pdf.model.PageConfig


fun getCountPxInOneCM(
    pageSideSize: Int,
    pagePaddingPercent: Float,
    maxDistance: Int,
): Float =
    (pageSideSize * (1 - 2 * pagePaddingPercent)) / maxDistance.roundUpToNearestToFullScale()

fun PageConfig.getCountPxInOneCM(
    shapeCanvas: ShapeCanvas,
): CountPxInOneCM {

    return CountPxInOneCM(
        x = getCountPxInOneCM(x, paddingPercentX, shapeCanvas.maxDistanceX.toInt()),
        y = getCountPxInOneCM(y, paddingPercentY, shapeCanvas.maxDistanceY.toInt())
    )
}
