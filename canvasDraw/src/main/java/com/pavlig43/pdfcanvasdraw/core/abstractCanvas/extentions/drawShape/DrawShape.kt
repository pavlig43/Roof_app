package com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape

import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import com.pavlig43.pdfcanvasdraw.DEFAULT_COLOR
import com.pavlig43.pdfcanvasdraw.FULL_ALPHA
import com.pavlig43.pdfcanvasdraw.RIGHT_DEGREE
import com.pavlig43.pdfcanvasdraw.SEMI_ALPHA
import com.pavlig43.pdfcanvasdraw.STROKE_WIDTH_MEDIUM
import com.pavlig43.pdfcanvasdraw.STROKE_WIDTH_THIN
import com.pavlig43.pdfcanvasdraw.TEXT_SIZE_SMALL
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.TextPainAbstract.Align
import com.pavlig43.pdfcanvasdraw.core.metrics.CountPxInOneCM
import com.pavlig43.pdfcanvasdraw.utils.zipWithNextCircular
import kotlin.math.cos
import kotlin.math.sin

fun CanvasInterface.drawShape(
    shapeOnCanvas: ShapeOnCanvas,
    countPxInOneCM: CountPxInOneCM,
    startPointF: PointF = PointF(0F, 0F),
    isPortrait: Boolean = false,
    shapePaint: ShapePaint = defaultShapePaint,
): List<PointF> {
    val paint =
        createPaint().apply {
            color = shapePaint.color
            strokeWidth = shapePaint.strokeWidth
            alpha = shapePaint.strokeAlpha
            style = shapePaint.style
        }

    val pointFListOfDots =
        shapeOnCanvas.listOfDots.map { pointF ->
            val generalY =
                if (isPortrait) (-pointF.y * countPxInOneCM.y) else (pointF.y * countPxInOneCM.y)
            PointF(
                pointF.x * countPxInOneCM.x + startPointF.x,
                generalY + startPointF.y,
            )
        }
    Log.d("pointFListOfDots", pointFListOfDots.toString())
    val pointFShapeCanvas = shapeOnCanvas.copy(pointFListOfDots)
    val path =
        createPath().apply {
            pointFListOfDots.zipWithNextCircular { first, second ->
                moveTo(first.x, first.y)
                lineTo(second.x, second.y)
            }
        }

    drawPath(path, paint)

    if (pointFShapeCanvas.nameValue.isNotBlank()) {
        drawTextOnCenterShape(
            pointFShapeCanvas,
        )
    }
    return pointFListOfDots
}

data class ShapePaint(
    val strokeWidth: Float = STROKE_WIDTH_MEDIUM,
    val strokeAlpha: Int = SEMI_ALPHA,
    val color: Int = Color.BLACK,
    val style: PaintAbstract.Style = PaintAbstract.Style.STROKE,
)

internal val defaultShapePaint = ShapePaint()
internal val thinShapePaint =
    ShapePaint(
        strokeWidth = STROKE_WIDTH_THIN,
        strokeAlpha = FULL_ALPHA,
        color = Color.RED,
    )

fun CanvasInterface.drawTextOnCenterShape(
    shapeOnCanvas: ShapeOnCanvas,
    rotateDegree: Float = RIGHT_DEGREE,
    drawTextParam: DrawTextParam = defaultDrawTextParam,
) {
    val paintText =
        createPaintText().apply {
            textSize = drawTextParam.textSize
        }

    /**
     *     длина текста в пикселях
     */
    val lenOfTextInPx = paintText.measureText(shapeOnCanvas.nameValue)

    /**
     * Смещение для середины тектса, используется, чтобы середина текста
     *   находилать посередине фигуры в зависимости от угла поворота
     */
    val pointFX = cos(Math.toRadians(rotateDegree.toDouble())) * lenOfTextInPx / 2
    val pointFY = sin(Math.toRadians(rotateDegree.toDouble())) * lenOfTextInPx / 2

    val x = shapeOnCanvas.peakXMin + shapeOnCanvas.height / 2 - pointFX
    val y = shapeOnCanvas.peakYMin + shapeOnCanvas.width / 2 - pointFY

    drawAndRotateText(
        degree = rotateDegree,
        pivotX = x.toFloat(),
        pivotY = y.toFloat(),
        text = shapeOnCanvas.nameValue,
        paintText = paintText
    )
}

data class DrawTextParam(
    val textColor: Int,
    val isUnderlineText: Boolean,
    val isStrikeThruText: Boolean,
    val letterSpacing: Float,
    val textSize: Float,
    val textAlign: Align,
)

private val defaultDrawTextParam =
    DrawTextParam(
        textSize = TEXT_SIZE_SMALL,
        textColor = DEFAULT_COLOR,
        isUnderlineText = false,
        isStrikeThruText = false,
        letterSpacing = 0F,
        textAlign = Align.CENTER,
    )
