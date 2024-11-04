package com.example.pdfcanvasdraw.abstractCanvas.drawKit

interface CanvasInterface {
    fun createPaint(): PaintAbstract
    fun createPaintText(): PaintTextAbstract
    fun createPath(): PathAbstract

    fun drawLine(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paint: PaintAbstract
    )

    fun drawText(
        text: String,
        x: Float,
        y: Float,
        paintText: PaintTextAbstract
    )

    fun rotate(
        degree: Float,
        pivotX: Float,
        pivotY: Float
    )

    fun drawPath(path: PathAbstract, paint: PaintAbstract)
}

abstract class PathAbstract {
    abstract fun moveTo(x: Float, y: Float)
    abstract fun lineTo(x: Float, y: Float)
    abstract fun close()
}

abstract class PaintAbstract {
    abstract var color: Int
    abstract var strokeWidth: Float
    abstract var style: Style
    abstract var alpha: Int


    enum class Style {
        STROKE, FILL
    }


}


abstract class PaintTextAbstract {
    abstract var textColor: Int
    abstract var isUnderlineText: Boolean
    abstract var isStrikeThruText: Boolean
    abstract var letterSpacing: Float
    abstract var textSize: Float
    abstract var textAlign: Align

    enum class Align {
        LEFT, CENTER, RIGHT
    }

    abstract fun measureText(text: String): Float


}



