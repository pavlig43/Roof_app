package com.example.pdfcanvasdraw.core.abstractCanvas.drawKit

import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.TextPainAbstract

interface CanvasInterface {
    fun createPaint(): PaintAbstract

    fun createPaintText(): TextPainAbstract

    fun createPath(): PathAbstract

    fun drawLine(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paint: PaintAbstract,
    )

    fun drawCircle(
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: PaintAbstract,
    )

    fun drawAndRotateText(
        text: String,
        paintText: TextPainAbstract,
        degree: Float = 0f,
        pivotX: Float = 0f,
        pivotY: Float = 0f,

    )

    fun drawPath(
        path: PathAbstract,
        paint: PaintAbstract,
    )
}
