package com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint

abstract class TextPainAbstract {
    abstract var textColor: Int
    abstract var isUnderlineText: Boolean
    abstract var isStrikeThruText: Boolean
    abstract var letterSpacing: Float
    abstract var textSize: Float
    abstract var textAlign: Align
    abstract var alpha: Int

    enum class Align {
        LEFT,
        CENTER,
        RIGHT,
    }

    abstract fun measureText(text: String): Float

    abstract fun getTextPaint(): Any
}
