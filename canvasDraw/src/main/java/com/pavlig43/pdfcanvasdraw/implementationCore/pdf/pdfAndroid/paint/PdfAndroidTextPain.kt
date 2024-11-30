package com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.paint

import android.graphics.Paint
import android.text.TextPaint
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.TextPainAbstract

open class PdfAndroidTextPain : TextPainAbstract() {
    private val textPaint = TextPaint()

    override var textColor: Int
        get() = textPaint.color
        set(value) {
            textPaint.color = value
        }

    override var isUnderlineText: Boolean
        get() = textPaint.isUnderlineText
        set(value) {
            textPaint.isUnderlineText = value
        }

    override var isStrikeThruText: Boolean
        get() = textPaint.isStrikeThruText
        set(value) {
            textPaint.isStrikeThruText = value
        }

    override var letterSpacing: Float
        get() = textPaint.letterSpacing
        set(value) {
            textPaint.letterSpacing = value
        }

    override var textSize: Float
        get() = textPaint.textSize
        set(value) {
            textPaint.textSize = value
        }

    override var textAlign: Align
        get() =
            when (textPaint.textAlign) {
                Paint.Align.LEFT -> Align.LEFT
                Paint.Align.CENTER -> Align.CENTER
                Paint.Align.RIGHT -> Align.RIGHT
                else -> Align.LEFT
            }
        set(value) {
            textPaint.textAlign =
                when (value) {
                    Align.LEFT -> Paint.Align.LEFT
                    Align.CENTER -> Paint.Align.CENTER
                    Align.RIGHT -> Paint.Align.RIGHT
                }
        }

    override fun measureText(text: String): Float {
        return textPaint.measureText(text)
    }

    override fun getTextPaint() = textPaint
}
