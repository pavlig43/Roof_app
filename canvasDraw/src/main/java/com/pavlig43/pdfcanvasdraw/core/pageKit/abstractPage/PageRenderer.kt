package com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.CM_IN_ONE_METER
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.metrics.getCountPxInOneCM

abstract class PageRenderer {
    abstract val pageConfig: PageConfig

    abstract suspend fun CanvasInterface.drawContent()

    protected fun countPxInOneCM(
        maxDistanceX: Int = 1,
        maxDistanceY: Int = 1,
        scale: Int = CM_IN_ONE_METER
    ) = pageConfig.getCountPxInOneCM(maxDistanceX, maxDistanceY, scale)

    suspend fun renderPage(canvas: CanvasInterface) {
        canvas.drawContent()
    }

    abstract fun handleGetTap(tapPointF: PointF)
}
