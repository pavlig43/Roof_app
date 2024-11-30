package com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.A4X
import com.pavlig43.pdfcanvasdraw.A4Y
import com.pavlig43.pdfcanvasdraw.PADDING_PERCENT

data class PageConfig(
    val x: Int = A4X,
    val y: Int = A4Y,
    val paddingPercentX: Float = PADDING_PERCENT,
    val paddingPercentY: Float = PADDING_PERCENT,
) {
    val startPointF = PointF(x * paddingPercentX, y * paddingPercentY)
}
