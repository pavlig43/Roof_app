package com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.metrics.getCountPxInOneCM

abstract class PageRenderer {
    abstract val pageConfig: PageConfig

    abstract suspend fun CanvasInterface.drawContent()

    protected fun countPxInOneCM(
        maxDistanceX: Int = 1,
        maxDistanceY: Int = 1,
    ) = pageConfig.getCountPxInOneCM(maxDistanceX, maxDistanceY)

    suspend fun renderPage(canvas: CanvasInterface) {
        canvas.drawContent()
    }

    abstract fun handleGetTap(tapPointF: PointF)
}
