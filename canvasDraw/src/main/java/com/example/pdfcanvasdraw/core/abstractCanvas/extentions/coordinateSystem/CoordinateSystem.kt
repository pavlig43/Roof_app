package com.example.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem

import android.graphics.Color
import android.graphics.PointF
import com.example.pdfcanvasdraw.CM_IN_ONE_METER
import com.example.pdfcanvasdraw.STROKE_WIDTH_MEDIUM_PLUS
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.example.pdfcanvasdraw.core.metrics.CountPxInOneCM
import com.example.pdfcanvasdraw.utils.extensions.roundUpToNearestWithScale

@Suppress("LongParameterList")
fun CanvasInterface.coordinateSystem(
    countPxInOneCM: CountPxInOneCM,
    countCMInX: Int,
    countCMInY: Int,
    startPointFLine: PointF = PointF(0f, 0f),
    rulerParam: RulerParam = RulerParam(),
    tickParam: TickParam = TickParam(),
    scaleRuler: Int = CM_IN_ONE_METER,
) {
    val endPointFLineX =
        PointF(
            (
                countCMInX.roundUpToNearestWithScale(scaleRuler).takeIf { it != 0 }
                    ?: scaleRuler
                ) * countPxInOneCM.x + startPointFLine.x,
            startPointFLine.y,
        )

    val endPointFLineY =
        PointF(
            startPointFLine.x,
            (
                countCMInY.roundUpToNearestWithScale(scaleRuler).takeIf { it != 0 }
                    ?: scaleRuler
                ) * countPxInOneCM.y + startPointFLine.y,
        )

    rulerLine(
        startPointFLine = startPointFLine,
        endPointFLine = endPointFLineX,
        countPxInOneCM = countPxInOneCM.x,
        countCM = countCMInX,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = true,
        tickParam = tickParam,
    )
    rulerLine(
        startPointFLine = startPointFLine,
        endPointFLine = endPointFLineY,
        countPxInOneCM = countPxInOneCM.y,
        countCM = countCMInY,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = false,
        tickParam = tickParam,
    )
}

data class RulerParam(
    val strokeWidth: Float = STROKE_WIDTH_MEDIUM_PLUS,
    val color: Int = Color.BLACK,
    val style: PaintAbstract.Style = PaintAbstract.Style.STROKE,
)
