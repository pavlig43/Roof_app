package com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.shape

import android.graphics.PointF
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.RulerParam
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.coordinateSystem.TickParam
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.ShapeOnCanvas
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.drawShapeWithRuler
import com.pavlig43.pdfcanvasdraw.core.metrics.CountPxInOneCM
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.hypot

class ConstructorShape(
    private val shapeOnCanvas: ShapeOnCanvas,
    override val pageConfig: PageConfig,
    val onClickDot: (Pair<Int, PointF>) -> Unit = { _ -> },
    private val radiusDot: Float = RADIUS_DOT,
    provideShapeOnRealCanvas: (CountPxInOneCM, PointF, Float) -> Unit = { _, _, _ -> }

) : PageRenderer() {
    private val startPointF = pageConfig.startPointF
    private val countPxInOneCM =
        countPxInOneCM(shapeOnCanvas.height.toInt(), shapeOnCanvas.width.toInt())
    private val listOfDotsOnRealCanvas = MutableStateFlow<List<PointF>>(listOf())

    init {
        provideShapeOnRealCanvas(
            countPxInOneCM,
            startPointF,
            radiusDot
        )
    }

    override suspend fun CanvasInterface.drawContent() {
        Log.d("startRenderer", shapeOnCanvas.toString())

        val updatedListOfDotsOnRealCanvas = drawShapeWithRuler(
            shapeOnCanvas = shapeOnCanvas,
            countPxInOneCM = countPxInOneCM,
            startPointF = startPointF,
            isPortrait = true,
            tickParam =
            TickParam().copy(
                textSize = TICK_TEXT_SIZE,
                paddingText = TICK_PADDING_TEXT,
            ),
            rulerParam = RulerParam().copy(strokeWidth = RULER_STROKE_WIDTH),
        )
        updatedListOfDotsOnRealCanvas.forEachIndexed { ind, dot ->
            drawCircle(
                dot.x,
                dot.y,
                radiusDot,
                createPaint().apply {
                    color = if (ind == 0) Color.Green.toArgb() else Color.Red.toArgb()
                    style = PaintAbstract.Style.FILL
                },
            )
        }
        listOfDotsOnRealCanvas.update { updatedListOfDotsOnRealCanvas }
    }

    override fun handleGetTap(tapPointF: PointF) {
        listOfDotsOnRealCanvas.value.forEachIndexed { index, pointF ->
            val distance =
                PointF(tapPointF.x - pointF.x, tapPointF.y - pointF.y).run { hypot(this.x, this.y) }
            if (distance < radiusDot) {
                onClickDot(Pair(index, pointF))
            }
        }
    }
}

private const val RADIUS_DOT = 20F
private const val RULER_STROKE_WIDTH = 8F
private const val TICK_PADDING_TEXT = 40F
private val TICK_TEXT_SIZE = 30.sp.value
