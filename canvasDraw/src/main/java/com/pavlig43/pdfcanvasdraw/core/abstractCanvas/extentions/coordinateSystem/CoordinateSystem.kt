package com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem

import android.graphics.Color
import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.CM_IN_ONE_METER
import com.pavlig43.pdfcanvasdraw.STROKE_WIDTH_MEDIUM_PLUS
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.pavlig43.pdfcanvasdraw.core.metrics.CountPxInOneCM
import com.pavlig43.pdfcanvasdraw.utils.extensions.roundUpToNearestWithScale

@Suppress("LongParameterList")
fun CanvasInterface.coordinateSystem(
    countPxInOneCM: CountPxInOneCM,
    countCMInX: Int,
    countCMInY: Int,
    startPointFLine: PointF = PointF(0f, 0f),
    isPortrait: Boolean = false,
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

    val absoluteEndPointFY = (countCMInY.roundUpToNearestWithScale(scaleRuler).takeIf { it != 0 }
        ?: scaleRuler) * countPxInOneCM.y
    val endPointFLineYAboutStartPoint = if (!isPortrait) absoluteEndPointFY else -absoluteEndPointFY
    val endPointFLineY =
        PointF(
            startPointFLine.x,
            startPointFLine.y + endPointFLineYAboutStartPoint
        )

    rulerLine(
        startPointFLine = startPointFLine,
        endPointFLine = endPointFLineX,
        countPxInOneCM = countPxInOneCM.x,
        countCM = countCMInX,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = true,
        isPortrait = isPortrait,
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
        isPortrait = isPortrait,
        tickParam = tickParam,
    )
}

data class RulerParam(
    val strokeWidth: Float = STROKE_WIDTH_MEDIUM_PLUS,
    val color: Int = Color.BLACK,
    val style: PaintAbstract.Style = PaintAbstract.Style.STROKE,
)
