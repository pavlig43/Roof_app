package com.pavlig43.roofapp.ui.shapes.quadrilateral

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.convertSheetDotToPx
import com.pavlig43.roofapp.model.replaceX
import com.pavlig43.roofapp.model.toPX
import com.pavlig43.roofapp.model.withOStartOffset
import com.pavlig43.roofapp.utils.drawSheet
import com.pavlig43.roofapp.utils.getSide
import com.pavlig43.roofapp.utils.rulerOnCanvasPDF
import com.pavlig43.roofapp.utils.searchDotsSheet
import com.pavlig43.roofapp.utils.searchInterpolation
import kotlin.math.abs
import kotlin.math.ceil

class QuadroPDF(
    geometry4SideShape: Geometry4SideShape,
    val sheet: Sheet,
) {
    private val widthPage = A4WIDTH
    private val heightPage = A4HEIGHT
    private val paddingWidth = (widthPage * 0.05).toFloat()
    private val paddingHeight = (heightPage * 0.05).toFloat()

    private val listOfSheets: MutableList<Sheet> = mutableListOf()

    /**
     * из 4 точек 4хугольника вычисляю минимальные значения точек , которые будут мспользованны
     * для поиска начала координат
     */
    private val startOffset: Offset =
        Offset(
            x =
                abs(
                    minOf(
                        geometry4SideShape.leftBottom.distanceX,
                        geometry4SideShape.rightBottom.distanceX,
                    ),
                ),
            y =
                abs(
                    minOf(
                        geometry4SideShape.leftBottom.distanceY,
                        geometry4SideShape.leftTop.distanceY,
                    ),
                ),
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

    private val leftSide = getSide(a, b)
    private val bottomSide = getSide(a, d)
    private val topSide = getSide(b, c)
    private val rightSide = getSide(c, d)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = maxOf(c.distanceY, d.distanceY)

    private val countCeilMetersWidth = ceil(maxWidthShape / 100).toInt()

    /**
     * число листов , которое будет положено по ширине фигуре, пока листы не закроют ее полностью
     */
    private val countOfSheet: Int =
        ceil((maxWidthShape - sheet.overlap) / sheet.visible).toInt()

    /**
     * Высота фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxHeightShape = maxOf(b.distanceX, c.distanceX)

    private val countCeilMetersHeight = ceil(maxHeightShape / 100).toInt()

    private val oneCMInHeightYtPx: Float =
        (heightPage * 0.9 / (countCeilMetersWidth * 100)).toFloat()

    private val oneCMInWidthXPx: Float =
        (widthPage * 0.9 / (countCeilMetersHeight * 100)).toFloat()

    fun ruler(
        canvas: Canvas,
        zero: Boolean = true,
        horizontal: Boolean = true,
    ) {
        rulerOnCanvasPDF(
            canvas,
            maxWidthShape = maxWidthShape,
            maxHeightShape = maxHeightShape,
            oneCMInWidthXPx = oneCMInWidthXPx,
            oneCMInHeightYtPx = oneCMInHeightYtPx,
            horizontal = horizontal,
            zero = zero,
        )
    }

    fun drawQuadro(
        canvas: Canvas,
        paint: Paint =
            Paint().apply {
                color = Color.BLACK
                strokeWidth = 4f
                alpha = 162 // Толщина линии
                style = Paint.Style.STROKE
            },
    ) {
        val aPX = a.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val bPX = b.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val cPX = c.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val dPX = d.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val path =
            android.graphics.Path().apply {
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

    /**
     * Выдает точки пересечения вертикальной стороны листа железа с каждой из сторон 4х-угольника
     * Если 4х-угольник правильный , то каждая вертикальная сторона листа имеет МАКСИМУМ  пересечений с 2 сторонами 4х-угольника
     * Если пересечение попадает на угол фигуры, то обе точки пересечения с фигурой одинаковые
     * Если пересечение отсутствует(в случае , когда правая сторона  последнего листа выходит за пределы фигуры)
     * то "Х" берется от левой стороны этого же листа [lastNotNullBottomX] и [lastNotNullTopX]
     */
    private fun searchLineOfSheet(
        y: Float,
        lastNotNullBottomX: Float = 0f,
        lastNotNullTopX: Float = 0f,
    ): Result<Pair<Offset, Offset>> {
        val intersectAB = searchInterpolation(a, b, y, a.distanceX)
        val intersectBC = searchInterpolation(b, c, y, b.distanceX)
        val intersectDC = searchInterpolation(d, c, y, d.distanceX)
        val intersectAD = searchInterpolation(a, d, y, a.distanceX)
        val lst =
            setOfNotNull(
                intersectAB,
                intersectBC,
                intersectDC,
                intersectAD,
            ).toList()
        Log.d("intersect", "$y\nab+$intersectAB\nbc+$intersectBC\ndc+$intersectDC\nad+$intersectAD")
        return when (lst.size) {
            2 -> Result.success(Pair(lst[0], lst[1]))
            1 -> Result.success(Pair(lst[0], lst[0]))
            0 ->
                Result.success(
                    Pair(
                        Offset(lastNotNullBottomX, y),
                        Offset(lastNotNullTopX, y),
                    ),
                )

            else -> Result.failure(IllegalStateException("Невозможно найти 2 пересечения"))
        }
    }

    /**
     * ищем последовательность "У" при котором "Х" находится на пике(минимальном или максимальном)
     */
    private fun findYPeakX(peak: Float): ClosedFloatingPointRange<Float> {
        val peakDots =
            listOf(a, b, c, d).asSequence().filter { it.distanceX == peak }
                .sortedBy { it.distanceY }.take(2).map { it.distanceY }
                .toList()
        return when (peakDots.size) {
            1 -> peakDots[0]..peakDots[0]
            2 -> peakDots[0]..peakDots[1]
            else -> 0f..0f
        }
    }

    fun sheetOnQuadro(canvas: Canvas) {
        val intervalYForXMax = findYPeakX(peakXMax)
        val intervalYForXMin = findYPeakX(peakXMin)
        for (s in 1..countOfSheet) {
            Log.d("rrrr", oneCMInHeightYtPx.toString())
            val y = (s - 1) * sheet.visible
            val resultLeft = searchLineOfSheet(y)
            val resultRight =
                searchLineOfSheet(
                    y = y + sheet.widthGeneral,
                    lastNotNullBottomX = resultLeft.getOrThrow().first.x,
                    lastNotNullTopX = resultLeft.getOrThrow().second.x,
                )
            val dotsOfSheet =
                searchDotsSheet(resultLeft, resultRight)
                    ?.replaceX(
                        peakXMin = peakXMin,
                        peakXMax = peakXMax,
                        intervalYForXMin = intervalYForXMin,
                        intervalYForXMax = intervalYForXMax,
                    )
            if (dotsOfSheet != null) {
                val sheetMultiplicityCM = sheet.multiplicity
                val lenOfSheetInCM = dotsOfSheet.leftTop.x - dotsOfSheet.leftBottom.x
                val lenOfSheet =
                    (ceil(lenOfSheetInCM / sheetMultiplicityCM) * sheetMultiplicityCM).toInt()
                listOfSheets.add(sheet.copy(len = lenOfSheet))
                drawSheet(
                    canvas = canvas,
                    sheetDots =
                        dotsOfSheet
                            .convertSheetDotToPx(
                                oneMeterInHeightYtPx = oneCMInHeightYtPx,
                                oneMeterInWidthXPx = oneCMInWidthXPx,
                            ),
                    lenOfSheet = lenOfSheet,
                )
            }
        }
    }

    fun getLstOfSheet() = listOfSheets

    fun getOtherParams(): List<Pair<String, String>> {
        return listOf(
            Pair("AB", "$leftSide cm"),
            Pair("BC", "$topSide cm"),
            Pair("CD", "$rightSide cm"),
            Pair("AD", "$bottomSide cm"),
            Pair("Высота фигуры ", "${maxHeightShape.toInt()} cm"),
            Pair("Ширина фигуры ", "${maxWidthShape.toInt()} cm"),
        )
    }
}

fun main() {
    val first = Offset(0f, 0f)
    val second = Offset(5f, 5f)
    val y = 3f
    val x = (first.x + (second.x - first.x) * (y - first.y)) / (second.y - first.y)
    println(x)
}
