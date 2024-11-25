package com.example.pdfcanvasdraw.core.pageKit.implementation.drawText

import android.graphics.PointF
import com.example.pdfcanvasdraw.TEXT_SIZE_MEDIUM_PLUS
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

class DrawText(
    private val info: List<String>,
    override val pageConfig: PageConfig = PageConfig(),
) : PageRenderer() {
    override suspend fun CanvasInterface.drawContent() {
        val rowSpacing = AddVerticalPaddingForLineText(pageConfig.startPointF.y)
        info.forEach {
            drawAndRotateText(
                it,
                pivotX = pageConfig.startPointF.x,
                pivotY = pageConfig.startPointF.y + rowSpacing.y,
                paintText = createPaintText().apply { textSize = TEXT_SIZE_MEDIUM_PLUS }
            )
            rowSpacing.addTransferText()
        }
    }

    override fun handleGetTap(tapPointF: PointF) {
        TODO("Not yet implemented")
    }
}
