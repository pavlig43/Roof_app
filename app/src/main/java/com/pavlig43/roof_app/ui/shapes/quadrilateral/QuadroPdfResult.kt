package com.pavlig43.roof_app.ui.shapes.quadrilateral

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.SheetDots
import com.pavlig43.roof_app.model.convertSheetDotToPx
import com.pavlig43.roof_app.model.withOStartOffset
import com.pavlig43.roof_app.utils.replaceX
import java.io.File
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt


fun pdfResult4Side(
    context: Context,
    geometry4SideShape: Geometry4SideShape,
    sheet: Sheet = Sheet()
): File {

    val quadroPDF = QuadroPDF(geometry4SideShape, sheet)
    val pdfDocument = PdfDocument()
    val pageInfo1 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 1).create()
    val page1 = pdfDocument.startPage(pageInfo1)
    val canvas1 = page1.canvas
    quadroPDF.ruler(canvas1)
    quadroPDF.ruler(canvas1, horizontal = false, zero = true)
    quadroPDF.drawQuadro(canvas1)
    quadroPDF.sheetOnQuadro(canvas1)
    pdfDocument.finishPage(page1)

    val file = File(context.getExternalFilesDir(null), "quadro.pdf")
    file.outputStream().use { pdfDocument.writeTo(it) }
    pdfDocument.close()
    return file


}

class QuadroPDF(
    geometry4SideShape: Geometry4SideShape,
    val sheet: Sheet
) {

    private val widthPage = A4WIDTH.toFloat()
    private val heightPage = A4HEIGHT.toFloat()
    private val paddingWidth = (widthPage * 0.05).toFloat()
    private val paddingHeight = (heightPage * 0.05).toFloat()

    /**
     * из 4 точек 4хугольника вычисляю минимальные значения точек , которые будут мспользованны
     * для поиска начала координат
     */
    private val startOffset: Offset =
        Offset(
            x = abs(
                minOf(
                    geometry4SideShape.leftBottom.distanceX,
                    geometry4SideShape.rightBottom.distanceX
                )
            ),
            y = abs(
                minOf(
                    geometry4SideShape.leftBottom.distanceY,
                    geometry4SideShape.leftTop.distanceY
                )
            )
        )



    private val a = geometry4SideShape.leftBottom.withOStartOffset(startOffset)
    private val b = geometry4SideShape.leftTop.withOStartOffset(startOffset)
    private val c = geometry4SideShape.rightTop.withOStartOffset(startOffset)
    private val d = geometry4SideShape.rightBottom.withOStartOffset(startOffset)

    /**
     * самая большая величина координаты высоты фигуры
     */
    private val peakXMax = listOf(a, b, c, d).maxOf { it.distanceX }

    /**
     * самая маленькая величина координаты высоты фигуры(низшая точка)
     */
    private val peakXMin = listOf(a, b, c, d).minOf { it.distanceX }

    private fun getSide(first: Dot, second: Dot): Int {
        return sqrt(
            (
                    ((second.distanceX - first.distanceX) * (second.distanceX - first.distanceX))
                            + ((second.distanceY - first.distanceY) * (second.distanceY - first.distanceY))).toDouble()
        ).toInt()
    }

    private val leftSide = getSide(a, b)
    private val bottomSide = getSide(a, d)
    private val topSide = getSide(b, c)
    private val rightSide = getSide(c, d)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = maxOf(c.distanceY, d.distanceY)

    /**
     * число листов , которое будет положено по ширине фигуре, пока листы не закроют ее полностью
     */
    private val countOfSheet: Int =
        ceil((maxWidthShape.toDouble() / 100) / sheet.visible + sheet.overlap).toInt()

    /**
     * Высота фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxHeightShape = maxOf(b.distanceX, c.distanceX)

    private val oneMeterInHeightYtPx: Float =
        (heightPage * 0.9 / ceil(maxWidthShape.toDouble() / 100)).toFloat()
    private val oneMeterInWidthXPx: Float =
        (widthPage * 0.9 / ceil(maxHeightShape.toDouble() / 100)).toFloat()

    fun ruler(
        canvas: Canvas,
        zero: Boolean = true,
        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f  // Толщина линии
            style = Paint.Style.STROKE
        },
        horizontal: Boolean = true,
    ) {
        if (!horizontal) {
            val x = paddingWidth
            val countMetres = ceil(maxWidthShape.toDouble() / 100).toInt()
            canvas.drawLine(
                x, paddingHeight,
                x, countMetres * oneMeterInHeightYtPx + paddingHeight,
                paint
            )
            for (m in 0..countMetres) {
                val y =
                    m.toFloat() * oneMeterInHeightYtPx + paddingHeight
                canvas.drawLine(x - 10, y, x + 10, y, Paint().apply { strokeWidth = 3f })
                Log.d("Drawing", "Drawing line at x: $x, y: $y")
                canvas.rotate(90f, x - 25, y - 10)
                when {
                    m == 0 && !zero -> continue
                    else -> canvas.drawText(
                        "$m м",
                        x - 25,
                        y - 10,
                        Paint().apply { textSize = 20f })
                }

                canvas.rotate(-90f, x - 25, y - 10)
            }

        } else {
            val y = paddingHeight
            val countMetres = ceil(maxHeightShape.toDouble() / 100).toInt()
            canvas.drawLine(
                paddingWidth,
                y,
                countMetres * oneMeterInWidthXPx + paddingWidth,
                y,
                paint
            )

            for (i in 0..countMetres) {
                val x = i.toFloat() * oneMeterInWidthXPx + paddingWidth
                canvas.drawLine(x, y - 10, x, y + 10, Paint().apply { strokeWidth = 3f })
                when {
                    i == 0 && !zero -> continue
                    else -> canvas.drawText(
                        "$i м",
                        x - 10,
                        y - 25,
                        Paint().apply { textSize = 20f })
                }

            }
        }

    }

    private fun Dot.toPX() = this.copy(
        distanceX = this.distanceX / 100 * oneMeterInWidthXPx,
        distanceY = this.distanceY / 100 * oneMeterInHeightYtPx
    )

    fun drawQuadro(
        canvas: Canvas,
        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            alpha = 162// Толщина линии
            style = Paint.Style.STROKE
        }
    ) {

        val aPX = a.toPX()
        val bPX = b.toPX()
        val cPX = c.toPX()
        val dPX = d.toPX()
        val path = android.graphics.Path().apply {
            moveTo(aPX.distanceX + paddingWidth, aPX.distanceY + paddingHeight)
            lineTo(bPX.distanceX + paddingWidth, bPX.distanceY + paddingHeight)
            moveTo(bPX.distanceX + paddingWidth, bPX.distanceY + paddingHeight)
            lineTo(cPX.distanceX + paddingWidth, cPX.distanceY + paddingHeight)
            moveTo(cPX.distanceX + paddingWidth, cPX.distanceY + paddingHeight)
            lineTo(dPX.distanceX + paddingWidth, dPX.distanceY + paddingHeight)
            moveTo(dPX.distanceX + paddingWidth, dPX.distanceY + paddingHeight)
            lineTo(aPX.distanceX + paddingWidth, aPX.distanceY + paddingHeight)
            close()
        }
        canvas.drawPath(path, paint)

    }

    private fun drawSheet(
        canvas: Canvas,
        sheetDots: SheetDots,
        lenOfSheet: Int

    ) {
        val leftBottom: Offset = sheetDots.leftBottom
        val leftTop: Offset = sheetDots.leftTop
        val rightTop: Offset = sheetDots.rightTop
        val rightBottom: Offset = sheetDots.rightBottom


        val paintOverlap: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 0.3f  // Толщина линии
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(
                floatArrayOf(10f, 20f), 0f
            )
        }
        val paintSheet: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 0.5f  // Толщина линии
            style = Paint.Style.STROKE
        }
        val path = android.graphics.Path().apply {
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

        val x = (leftTop.x - leftBottom.x) / 2 + leftBottom.x / 2
        val y = (leftTop.y - rightTop.y) / 2 + rightTop.y
        canvas.rotate(90f, x, y)
        canvas.drawText("$lenOfSheet cm", x, y, paintSheet)
        canvas.rotate(-90f, x, y)

    }

    private fun searchInterpolation(first: Dot, second: Dot, y: Float, constX: Float): Offset? {
        val (dotOne, dotTwo) = if (first.distanceY < second.distanceY) {
            Pair(first, second) // Если first левее по Y, то dotOne = first, dotTwo = second
        } else {
            Pair(second, first) // Если second левее по Y, то dotOne = second, dotTwo = first
        }
        when {
            dotOne.distanceY > y || dotTwo.distanceY < y -> return null
            dotOne.distanceY == dotTwo.distanceY -> return Offset(constX, y)
            else -> {
                val x =
                    first.distanceX + (second.distanceX - first.distanceX) * (y - first.distanceY) / (second.distanceY - first.distanceY)
                return Offset(abs(x), y)
            }
        }
    }

    private fun searchLineOfSheet(
        y: Float,
        lastNotNullBottomX: Float = 0f,
        lastNotNullTopX: Float = 0f

    ): Result<Pair<Offset, Offset>> {
        val intersectAB = searchInterpolation(a, b, y, a.distanceX / 100.toFloat())
        val intersectBC = searchInterpolation(b, c, y, b.distanceX / 100.toFloat())
        val intersectDC = searchInterpolation(d, c, y, d.distanceX)
        val intersectAD = searchInterpolation(a, d, y, a.distanceX)
        val lst = setOfNotNull(
            intersectAB,
            intersectBC,
            intersectDC,
            intersectAD,
        ).toList()
        Log.d("intersect", "$y\nab+$intersectAB\nbc+$intersectBC\ndc+$intersectDC\nad+$intersectAD")
        return when (lst.size) {
            2 -> Result.success(Pair(lst[0], lst[1]))
            1 -> Result.success(Pair(lst[0], lst[0]))
            0 -> Result.success(
                Pair(
                    Offset(lastNotNullBottomX, y),
                    Offset(lastNotNullTopX, y)
                )
            )


            else -> Result.failure(IllegalStateException("Невозможно найти 2 пересечения"))
        }

    }

    private fun searchDotsSheet(y: Float): SheetDots? {
        val resultLeft = searchLineOfSheet(y)
        val resultRight = searchLineOfSheet(
            y = (y + sheet.widthGeneral * 100).toFloat(),
            lastNotNullBottomX = resultLeft.getOrThrow().first.x,
            lastNotNullTopX = resultLeft.getOrThrow().second.x,
        )
        if (resultLeft.isSuccess && resultRight.isSuccess) {
            val firstLeft = resultLeft.getOrThrow().first
            val secondLeft = resultLeft.getOrThrow().second
            val firstRight = resultRight.getOrThrow().first
            val secondRight = resultRight.getOrThrow().second
            val leftBottom = Offset(
                minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                minOf(firstLeft.y, secondRight.y, secondLeft.y, secondRight.y)

            )
            val leftTop = Offset(
                maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                minOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
            )
            val rightTop = Offset(
                maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
            )
            val rightBottom = Offset(
                minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
            )

            val sheetDots = SheetDots(
                leftBottom = leftBottom,
                leftTop = leftTop,
                rightTop = rightTop,
                rightBottom = rightBottom,
            )
            Log.d("dotSheet", sheetDots.toString())
            return sheetDots

        } else {
            Log.d("resultRight", resultRight.exceptionOrNull().toString())
            Log.d("resultLeft", resultLeft.exceptionOrNull().toString())
            return null
        }
    }

    /**
     * ищем у при котором х максимальный
     */
    private fun findYPeakX(maxPeak: Float): ClosedFloatingPointRange<Float> {


        val peakDots =
            listOf(a, b, c, d).asSequence().filter { it.distanceX == maxPeak }
                .sortedBy { it.distanceY }.take(2).map { it.distanceY }
                .toList()
        return when (peakDots.size) {
            1 -> peakDots[0]..peakDots[0]
            2 -> peakDots[0]..peakDots[1]
            else -> 0f..0f
        }

    }


    fun sheetOnQuadro(
        canvas: Canvas
    ) {
        val intervalYForXMax = findYPeakX(peakXMax)
        val intervalYForXMin = findYPeakX(peakXMin)
        for (s in 1..countOfSheet) {
            val y = (s - 1) * sheet.visible * 100
            val dotsOfSheet = searchDotsSheet(y.toFloat())
            if (dotsOfSheet != null) {
                val sheetMultiplicityCM = sheet.multiplicity * 100
                val lenOfSheetInCM = dotsOfSheet.leftTop.x - dotsOfSheet.leftBottom.x
                val lenOfSheet =
                    (ceil(lenOfSheetInCM / sheetMultiplicityCM) * sheetMultiplicityCM).toInt()
                drawSheet(
                    canvas = canvas,
                    sheetDots = dotsOfSheet.replaceX(
                        peakXMin = peakXMin,
                        peakXMax = peakXMax,
                        intervalYForXMin = intervalYForXMin,
                        intervalYForXMax = intervalYForXMax
                    )
                        .convertSheetDotToPx(
                            oneMeterInHeightYtPx = oneMeterInHeightYtPx,
                            oneMeterInWidthXPx = oneMeterInWidthXPx,
                        ),
                    lenOfSheet = lenOfSheet
                )
            }

        }

    }


}


fun SheetDots.replaceX(
    peakXMax: Float,
    peakXMin: Float,
    intervalYForXMax: ClosedFloatingPointRange<Float>,
    intervalYForXMin: ClosedFloatingPointRange<Float>,

    ): SheetDots {
    val sheetIntervalY = this.leftTop.y..this.rightTop.y
    var result = this
    if (sheetIntervalY.start <= intervalYForXMax.endInclusive && intervalYForXMax.start <= sheetIntervalY.endInclusive) {
        result = result.copy(
            leftTop = this.leftTop.replaceX(peakXMax),
            rightTop = this.rightTop.replaceX(peakXMax)
        )
    }
    if (sheetIntervalY.start <= intervalYForXMin.endInclusive && intervalYForXMin.start <= sheetIntervalY.endInclusive) {
        result = result.copy(
            leftBottom = this.leftBottom.replaceX(peakXMin),
            rightBottom = this.rightBottom.replaceX(peakXMin)
        )
    }
    return result
}


fun main() {
    val first = Offset(0f, 0f)
    val second = Offset(5f, 5f)
    val y = 3f
    val x = (first.x + (second.x - first.x) * (y - first.y)) / (second.y - first.y)
    println(x)
}