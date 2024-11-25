package com.example.pdfcanvasdraw.core.pageKit.abstractPage

import android.graphics.PointF
import com.example.pdfcanvasdraw.A4X
import com.example.pdfcanvasdraw.A4Y
import com.example.pdfcanvasdraw.PADDING_PERCENT

data class PageConfig(
    val x: Int = A4X,
    val y: Int = A4Y,
    val paddingPercentX: Float = PADDING_PERCENT,
    val paddingPercentY: Float = PADDING_PERCENT,
) {
    val startPointF = PointF(x * paddingPercentX, y * paddingPercentY)
}
