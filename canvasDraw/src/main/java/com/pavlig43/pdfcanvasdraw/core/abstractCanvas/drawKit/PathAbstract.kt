package com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit

abstract class PathAbstract {
    abstract fun moveTo(
        x: Float,
        y: Float,
    )

    abstract fun lineTo(
        x: Float,
        y: Float,
    )

    abstract fun close()

    abstract fun getPath(): Any
}
