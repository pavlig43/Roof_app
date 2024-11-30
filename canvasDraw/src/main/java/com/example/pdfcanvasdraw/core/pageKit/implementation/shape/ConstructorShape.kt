package com.example.pdfcanvasdraw.core.pageKit.implementation.shape

import android.graphics.PointF
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.example.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.RulerParam
import com.example.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.TickParam
import com.example.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.ShapeOnCanvas
import com.example.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.drawShapeWithRuler
import com.example.pdfcanvasdraw.core.metrics.CountPxInOneCM
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer
import kotlin.math.hypot

class ConstructorShape(
    private val shapeOnCanvas: ShapeOnCanvas,
    override val pageConfig: PageConfig,
    val onClickDot: (Pair<Int, PointF>) -> Unit = { _ -> },
    provideShapeOnRealCanvas: (CountPxInOneCM, PointF, Float) -> Unit = { _, _, _ -> }

) : PageRenderer() {
    private val startPointF = pageConfig.startPointF
    private val countPxInOneCM =
        countPxInOneCM(shapeOnCanvas.height.toInt(), shapeOnCanvas.width.toInt())
    private val listOfDotsOnRealCanvas =
        shapeOnCanvas.listOfDots.map { dot ->
            val centerX = dot.x * countPxInOneCM.x + startPointF.x
            val centerY = dot.y * countPxInOneCM.y + startPointF.y
            PointF(centerX, centerY)
        }

    init {
        provideShapeOnRealCanvas(
            countPxInOneCM,
            startPointF,
            RADIUS_DOT
        )
    }

    override suspend fun CanvasInterface.drawContent() {
        Log.d("startRenderer", shapeOnCanvas.toString())

        drawShapeWithRuler(
            shapeOnCanvas = shapeOnCanvas,
            countPxInOneCM = countPxInOneCM,
            startPointF = startPointF,
            tickParam =
            TickParam().copy(
                textSize = TICK_TEXT_SIZE,
                paddingText = TICK_PADDING_TEXT,
            ),
            rulerParam = RulerParam().copy(strokeWidth = RULER_STROKE_WIDTH),
        )

        listOfDotsOnRealCanvas.forEachIndexed { ind, dot ->

            drawCircle(
                dot.x,
                dot.y,
                RADIUS_DOT,
                createPaint().apply {
                    color = if (ind == 0) Color.Green.toArgb() else Color.Red.toArgb()
                    style = PaintAbstract.Style.FILL
                },
            )
        }
    }

    override fun handleGetTap(tapPointF: PointF) {
        listOfDotsOnRealCanvas.forEachIndexed { index, pointF ->
            val distance =
                PointF(tapPointF.x - pointF.x, tapPointF.y - pointF.y).run { hypot(this.x, this.y) }
            if (distance < RADIUS_DOT) {
                onClickDot(Pair(index, pointF))
            }
        }
    }
}

private const val RADIUS_DOT = 20F
private const val RULER_STROKE_WIDTH = 8F
private const val TICK_PADDING_TEXT = 40F
private val TICK_TEXT_SIZE = 30.sp.value
