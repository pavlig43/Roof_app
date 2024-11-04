package com.example.pdfcanvasdraw.abstractCanvas.сoordinateSystem


import android.graphics.Color
import android.graphics.PointF
import com.example.pdfcanvasdraw.CM_IN_ONE_METER
import com.example.pdfcanvasdraw.STROKE_WIDTH_MEDIUM_PLUS
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.PaintAbstract
import com.example.pdfcanvasdraw.pdf.model.CountPxInOneCM


@Suppress("LongParameterList")
fun CanvasInterface.coordinateSystem(
    countPxInOneCM: CountPxInOneCM,
    countCMInX: Int,
    countCMInY: Int,
    startPointFLine: PointF = PointF(0f, 0f),
    rulerParam: RulerParam = RulerParam(),
    scaleRuler: Int = CM_IN_ONE_METER,
) {
    val endPointFLineX =
        PointF(countCMInX * countPxInOneCM.x + startPointFLine.x, startPointFLine.y)

    val endPointFLineY =
        PointF(startPointFLine.x, countCMInY * countPxInOneCM.y + startPointFLine.y)

    rulerLine(
        startPointFLine = startPointFLine,
        endPointFLine = endPointFLineX,

        countPxInOneCM = countPxInOneCM.x,
        countCM = countCMInX,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = true,

        )
    rulerLine(
        startPointFLine = startPointFLine,
        endPointFLine = endPointFLineY,
        countPxInOneCM = countPxInOneCM.y,
        countCM = countCMInY,
        rulerParam = rulerParam,
        scaleRuler = scaleRuler,
        forXRuler = false,
    )
}

data class RulerParam(
    val strokeWidth: Float = STROKE_WIDTH_MEDIUM_PLUS,
    val color: Int = Color.BLACK,
    val style: PaintAbstract.Style = PaintAbstract.Style.STROKE
)


