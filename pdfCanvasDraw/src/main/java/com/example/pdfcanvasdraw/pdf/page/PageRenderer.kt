package com.example.pdfcanvasdraw.pdf.page

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.text.TextPaint
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.PaintAbstract
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.PaintTextAbstract
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.PathAbstract
import com.example.pdfcanvasdraw.pdf.model.PageConfig


abstract class PageRenderer {
    abstract val pageConfig: PageConfig
    abstract fun CanvasInterface.drawContent()
    fun renderPage(pdfDocument: PdfDocument, pageNumber: Int = 1) {
        val pageInfo = PageInfo.Builder(pageConfig.x, pageConfig.y, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = AndroidCanvas(page.canvas)
        canvas.drawContent()
        pdfDocument.finishPage(page)


    }
}

class PageRendererAndroidPdf(override val pageConfig: PageConfig) : PageRenderer() {
    override fun CanvasInterface.drawContent() {
        TODO("Not yet implemented")
    }
}

abstract class PageRenderer1 {
    abstract val pageConfig: PageConfig
    abstract suspend fun CanvasInterface.drawContent()
    suspend fun renderPage(pdfDocument: PdfDocument, pageNumber: Int) {

        val pageInfo = PageInfo.Builder(pageConfig.x, pageConfig.y, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = AndroidCanvas(page.canvas)
        canvas.drawContent()

        pdfDocument.finishPage(page)


    }
}


class AndroidCanvas(private val canvas: Canvas) : CanvasInterface {
    override fun createPaint(): PaintAbstract {
        return AndroidPaint()
    }

    override fun createPaintText(): PaintTextAbstract {
        return AndroidPaintText()
    }

    override fun createPath(): PathAbstract {
        return AndroidPath()
    }

    override fun drawLine(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paint: PaintAbstract
    ) {
        if (paint is AndroidPaint) {
            canvas.drawLine(startX, startY, endX, endY, paint.getPaint())
        }

    }

    override fun drawText(text: String, x: Float, y: Float, paintText: PaintTextAbstract) {
        if (paintText is AndroidPaintText) {
            canvas.drawText(text, x, y, paintText.getPaint())
        }
    }

    override fun rotate(degree: Float, pivotX: Float, pivotY: Float) {
        canvas.rotate(degree, pivotX, pivotY)
    }

    override fun drawPath(path: PathAbstract, paint: PaintAbstract) {
        if (path is AndroidPath && paint is AndroidPaint) {
            canvas.drawPath(path.getPath(), paint.getPaint())
        }
    }

    class AndroidPaint : PaintAbstract() {
        private val paint = Paint()

        override var color: Int
            get() = paint.color
            set(value) {
                paint.color = value
            }

        override var strokeWidth: Float
            get() = paint.strokeWidth
            set(value) {
                paint.strokeWidth = value
            }

        override var style: Style
            get() = when (paint.style) {
                Paint.Style.STROKE -> Style.STROKE
                Paint.Style.FILL -> Style.FILL
                else -> Style.STROKE // По умолчанию
            }
            set(value) {
                paint.style = when (value) {
                    Style.STROKE -> Paint.Style.STROKE
                    Style.FILL -> Paint.Style.FILL
                }
            }

        override var alpha: Int
            get() = paint.alpha
            set(value) {
                paint.alpha = value
            }

        fun getPaint(): Paint = paint
    }

    class AndroidPaintText : PaintTextAbstract() {
        private val paintText = TextPaint()

        override var textColor: Int
            get() = paintText.color
            set(value) {
                paintText.color = value
            }

        override var isUnderlineText: Boolean
            get() = paintText.isUnderlineText
            set(value) {
                paintText.isUnderlineText = value
            }

        override var isStrikeThruText: Boolean
            get() = paintText.isStrikeThruText
            set(value) {
                paintText.isStrikeThruText = value
            }

        override var letterSpacing: Float
            get() = paintText.letterSpacing
            set(value) {
                paintText.letterSpacing = value
            }

        override var textSize: Float
            get() = paintText.textSize
            set(value) {
                paintText.textSize = value
            }

        override var textAlign: Align
            get() = when (paintText.textAlign) {
                Paint.Align.LEFT -> Align.LEFT
                Paint.Align.CENTER -> Align.CENTER
                Paint.Align.RIGHT -> Align.RIGHT
                else -> Align.LEFT
            }
            set(value) {
                paintText.textAlign = when (value) {
                    Align.LEFT -> Paint.Align.LEFT
                    Align.CENTER -> Paint.Align.CENTER
                    Align.RIGHT -> Paint.Align.RIGHT
                }
            }

        override fun measureText(text: String): Float {
            return paintText.measureText(text)
        }

        fun getPaint(): TextPaint = paintText
    }

    class AndroidPath : PathAbstract() {
        private val path = Path()

        override fun moveTo(x: Float, y: Float) {
            path.moveTo(x, y)
        }

        override fun lineTo(x: Float, y: Float) {
            path.lineTo(x, y)
        }

        override fun close() {
            path.close()
        }

        // Метод для получения Android Path, если понадобится
        fun getPath(): Path = path
    }
}

