package com.example.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.paint

import android.graphics.Paint
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract

class PdfAndroidPaint : PaintAbstract() {
    private val innerPaint = Paint()

    override var color: Int
        get() = innerPaint.color
        set(value) {
            innerPaint.color = value
        }

    override var strokeWidth: Float
        get() = innerPaint.strokeWidth
        set(value) {
            innerPaint.strokeWidth = value
        }

    override var style: Style
        get() =
            when (innerPaint.style) {
                Paint.Style.STROKE -> Style.STROKE
                Paint.Style.FILL -> Style.FILL
                else -> Style.STROKE // default
            }
        set(value) {
            innerPaint.style =
                when (value) {
                    Style.STROKE -> Paint.Style.STROKE
                    Style.FILL -> Paint.Style.FILL
                }
        }

    override var alpha: Int
        get() = innerPaint.alpha
        set(value) {
            innerPaint.alpha = value
        }

    override fun getPaint(): Paint = innerPaint
}
