package com.example.pdfcanvasdraw.abstractCanvas.сoordinateSystem


import android.graphics.PointF
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.extensions.roundUpToNearestWithScale

@Suppress("LongParameterList")
fun CanvasInterface.rulerLine(
    startPointFLine: PointF,
    endPointFLine: PointF,
    countPxInOneCM: Float,
    countCM: Int,
    rulerParam: RulerParam,
    scaleRuler: Int,
    forXRuler: Boolean,

    ) {
    val countCeilOtherUnit = countCM.roundUpToNearestWithScale(scaleRuler)
    val paint = createPaint().apply {
        color = rulerParam.color
        strokeWidth = rulerParam.strokeWidth
        style = rulerParam.style
    }

    drawLine(
        startPointFLine.x,
        startPointFLine.y,
        endPointFLine.x,
        endPointFLine.y,
        paint
    )

    for (i in 0..countCeilOtherUnit) {
        val pointFTick = if (forXRuler) {
            PointF(i * countPxInOneCM * scaleRuler + startPointFLine.x, startPointFLine.y)
        } else {
            PointF(startPointFLine.x, i * countPxInOneCM * scaleRuler + startPointFLine.y)
        }

        this.tickForRuler(
            numberOfTick = i,
            x = pointFTick.x,
            y = pointFTick.y,
            forXRuler = forXRuler
        )
    }
}
