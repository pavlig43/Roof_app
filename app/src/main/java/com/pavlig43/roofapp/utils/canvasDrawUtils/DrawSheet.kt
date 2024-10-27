package com.pavlig43.roofapp.utils.canvasDrawUtils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.text.TextPaint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.DASH_INTERVALS
import com.pavlig43.roofapp.RIGHT_ANGLE
import com.pavlig43.roofapp.STROKE_WIDTH_THIN_PDF_CANVAS
import com.pavlig43.roofapp.TEXT_SIZE_SMALL
import com.pavlig43.roofapp.model.SheetDots
import com.pavlig43.roofapp.utils.toOffset
import java.math.BigDecimal

/**
 * Рисует на канвасе декартову плоскость с насечками метров,
 * передаем максимальную шрину и высоту фигуры в см
 * передаем значение пикселей в  1см по  "у" и по "х"
 * (можно и тут рассчитывать, но это значение нужно будет в основной фигуре)
 */

/**
 * Рисует на канвасе лист железа, передается координаты всех четырех точек
 */
@Suppress("LongParameterList")
fun drawSheet(
    canvas: Canvas,
    sheetDots: SheetDots,
    lenOfSheet: BigDecimal,
    paintOverlap: Paint =
        Paint().apply {
            color = Color.RED
            strokeWidth = STROKE_WIDTH_THIN_PDF_CANVAS
            style = Paint.Style.STROKE
            pathEffect =
                DashPathEffect(
                    DASH_INTERVALS,
                    0f,
                )
        },
    paintSheet: Paint =
        Paint().apply {
            color = Color.RED
            strokeWidth = STROKE_WIDTH_THIN_PDF_CANVAS
            style = Paint.Style.STROKE
        },
    paintText: TextPaint =
        TextPaint().apply {
            textSize = TEXT_SIZE_SMALL
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        },
) {
    val leftBottom: Offset = sheetDots.leftBottom.toOffset()
    val leftTop: Offset = sheetDots.leftTop.toOffset()
    val rightTop: Offset = sheetDots.rightTop.toOffset()
    val rightBottom: Offset = sheetDots.rightBottom.toOffset()

    val path =
        android.graphics.Path().apply {
            moveTo(rightBottom.x, rightBottom.y)
            lineTo(leftBottom.x, leftBottom.y)
            moveTo(leftBottom.x, leftBottom.y)
            lineTo(leftTop.x, leftTop.y)
            moveTo(leftTop.x, leftTop.y)
            lineTo(rightTop.x, rightTop.y)
            close()
        }
    canvas.drawPath(path, paintSheet)
    canvas.drawLine(rightTop.x, rightTop.y, rightBottom.x, rightBottom.y, paintOverlap)

    val lenOfSheetInPX =
        paintText.measureText(
            "$lenOfSheet cm",
        )
    // длина текста в пикселях , используется, чтобы середина текста
    // находилать посередине листа, без этого параметра сам текст начинается в середине листа,
    // а  оканчивается за пределами листа
    val x = (leftTop.x - leftBottom.x) / 2 + leftBottom.x / 2
    val y = (leftTop.y - rightTop.y) / 2 + rightTop.y - lenOfSheetInPX / 2

    Log.d("lenOfSheetInPX", lenOfSheetInPX.toString())
    canvas.rotate(RIGHT_ANGLE, x, y)
    canvas.drawText("$lenOfSheet cm", x, y, paintText)
    canvas.rotate(-RIGHT_ANGLE, x, y)
}
