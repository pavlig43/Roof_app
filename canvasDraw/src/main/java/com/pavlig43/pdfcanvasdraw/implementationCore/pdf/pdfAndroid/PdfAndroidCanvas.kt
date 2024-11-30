package com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid

import android.graphics.Canvas
import android.graphics.Color
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.PathAbstract
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.TextPainAbstract
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.paint.PdfAndroidPaint
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.paint.PdfAndroidTextPain

class PdfAndroidCanvas(private val canvas: Canvas) : CanvasInterface {
    override fun createPaint(): PaintAbstract {
        return PdfAndroidPaint()
    }

    override fun createPaintText(): TextPainAbstract {
        return PdfAndroidTextPain()
    }

    override fun createPath(): PathAbstract {
        return PdfAndroidPath()
    }

    override fun drawLine(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paint: PaintAbstract,
    ) {
        if (paint is PdfAndroidPaint) {
            canvas.drawLine(startX, startY, endX, endY, paint.getPaint())
        }
    }

    override fun drawCircle(
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: PaintAbstract,
    ) {
        if (paint is PdfAndroidPaint) {
            val innerPaint = paint.getPaint()
            canvas.drawCircle(
                centerX,
                centerX,
                radius,
                innerPaint.apply { color = Color.RED },
            )
        }
    }

    override fun drawAndRotateText(
        text: String,
        paintText: TextPainAbstract,
        degree: Float,
        pivotX: Float,
        pivotY: Float,
    ) {
        if (paintText is PdfAndroidTextPain) {
            canvas.rotate(degree, pivotX, pivotY)
            canvas.drawText(text, pivotX, pivotY, paintText.getTextPaint())
            canvas.rotate(-degree, pivotX, pivotY)
        }
    }

    override fun drawPath(
        path: PathAbstract,
        paint: PaintAbstract,
    ) {
        if (path is PdfAndroidPath && paint is PdfAndroidPaint) {
            canvas.drawPath(path.getPath(), paint.getPaint())
        }
    }
}
