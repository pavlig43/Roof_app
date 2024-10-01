package com.pavlig43.roofapp.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.text.TextPaint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH
import com.pavlig43.roofapp.model.SheetDots
import kotlin.math.ceil

/**
 * Рисует на канвасе декартову плоскость с насечками метров,
 * передаем максимальную шрину и высоту фигуры в см
 * передаем значение пикселей в  1см по  "у" и по "х"(можно и тут рассчитывать, но это значение нужно будет в основной фигуре)
 */
fun rulerOnCanvasPDF(
    canvas: Canvas,
    maxWidthShape: Float,
    maxHeightShape: Float,
    oneCMInHeightYtPx: Float,
    oneCMInWidthXPx: Float,
    zero: Boolean = true,
    paint: Paint =
        Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f // Толщина линии
            style = Paint.Style.STROKE
        },
    horizontal: Boolean = true,
    widthPage: Int = A4WIDTH,
    heightPage: Int = A4HEIGHT,
    paddingWidth: Float = (widthPage * 0.05).toFloat(),
    paddingHeight: Float = (heightPage * 0.05).toFloat(),
) {
    val countCeilMetersWidth = ceil(maxWidthShape / 100).toInt()
    val countCeilMetersHeight = ceil(maxHeightShape / 100).toInt()
    if (!horizontal) {
        canvas.drawLine(
            paddingWidth,
            paddingHeight,
            paddingWidth,
            countCeilMetersWidth * oneCMInHeightYtPx * 100 + paddingHeight,
            paint,
        )
        for (m in 0..countCeilMetersWidth) {
            val y =
                m.toFloat() * oneCMInHeightYtPx * 100 + paddingHeight
            canvas.drawLine(
                paddingWidth - 10,
                y,
                paddingWidth + 10,
                y,
                Paint().apply { strokeWidth = 3f },
            )
            Log.d("Drawing", "Drawing line at x: $paddingWidth, y: $y")
            canvas.rotate(90f, paddingWidth - 25, y - 10)
            when {
                m == 0 && !zero -> continue
                else ->
                    canvas.drawText(
                        "$m м",
                        paddingWidth - 25,
                        y - 10,
                        Paint().apply { textSize = 20f },
                    )
            }

            canvas.rotate(-90f, paddingWidth - 25, y - 10)
        }
    } else {
        canvas.drawLine(
            paddingWidth,
            paddingHeight,
            countCeilMetersHeight * oneCMInWidthXPx * 100 + paddingWidth,
            paddingHeight,
            paint,
        )

        for (i in 0..countCeilMetersHeight) {
            val x = i.toFloat() * oneCMInWidthXPx * 100 + paddingWidth
            canvas.drawLine(
                x,
                paddingHeight - 10,
                x,
                paddingHeight + 10,
                Paint().apply { strokeWidth = 3f },
            )
            when {
                i == 0 && !zero -> continue
                else ->
                    canvas.drawText(
                        "$i м",
                        x - 10,
                        paddingHeight - 25,
                        Paint().apply { textSize = 20f },
                    )
            }
        }
    }
}

/**
 * Рисует на канвасе лист железа, передается координаты всех четырех точек
 */
fun drawSheet(
    canvas: Canvas,
    sheetDots: SheetDots,
    lenOfSheet: Int,
    paintOverlap: Paint =
        Paint().apply {
            color = Color.RED
            strokeWidth = 1f // Толщина линии
            style = Paint.Style.STROKE
            pathEffect =
                DashPathEffect(
                    floatArrayOf(10f, 20f), 0f,
                )
        },
    paintSheet: Paint =
        Paint().apply {
            color = Color.RED
            strokeWidth = 1f // Толщина линии
            style = Paint.Style.STROKE
        },
    paintText: TextPaint =
        TextPaint().apply {
            textSize = 12f
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        },
) {
    val leftBottom: Offset = sheetDots.leftBottom
    val leftTop: Offset = sheetDots.leftTop
    val rightTop: Offset = sheetDots.rightTop
    val rightBottom: Offset = sheetDots.rightBottom

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
        ) // длина текста в пикселях , используется, чтобы середина текста находилать посередине листа, без этого параметра сам текст начинается в середине листа, а  оканчивается за пределами листа
    val x = (leftTop.x - leftBottom.x) / 2 + leftBottom.x / 2
    val y = (leftTop.y - rightTop.y) / 2 + rightTop.y - lenOfSheetInPX / 2

    Log.d("lenOfSheetInPX", lenOfSheetInPX.toString())
    canvas.rotate(90f, x, y)
    canvas.drawText("$lenOfSheet cm", x, y, paintText)
    canvas.rotate(-90f, x, y)
}
