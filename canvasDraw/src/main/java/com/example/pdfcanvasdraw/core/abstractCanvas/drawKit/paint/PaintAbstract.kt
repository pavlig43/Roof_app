package com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint

abstract class PaintAbstract {
    abstract var color: Int

    abstract var strokeWidth: Float
    abstract var style: Style
    abstract var alpha: Int

    enum class Style {
        STROKE,
        FILL,
    }

    abstract fun getPaint(): Any
}
