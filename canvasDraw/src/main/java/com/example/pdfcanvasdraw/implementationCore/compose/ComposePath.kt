package com.example.pdfcanvasdraw.implementationCore.compose

import androidx.compose.ui.graphics.Path
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.PathAbstract

class ComposePath : PathAbstract() {
    private val path = Path()

    override fun moveTo(
        x: Float,
        y: Float,
    ) {
        path.apply { moveTo(x, y) }
    }

    override fun lineTo(
        x: Float,
        y: Float,
    ) {
        path.apply { lineTo(x, y) }
    }

    override fun close() {
        path.apply { close() }
    }

    override fun getPath() = path
}
