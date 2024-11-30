package com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid

import android.graphics.Path
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.PathAbstract

class PdfAndroidPath : PathAbstract() {
    private val path = Path()

    override fun moveTo(
        x: Float,
        y: Float,
    ) {
        path.moveTo(x, y)
    }

    override fun lineTo(
        x: Float,
        y: Float,
    ) {
        path.lineTo(x, y)
    }

    override fun close() {
        path.close()
    }

    override fun getPath(): Path = path
}
