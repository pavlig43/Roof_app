package com.pavlig43.roofapp.ui.shapes.quadrilateral

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.mathbigdecimal.OffsetBD

import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH
import com.pavlig43.roofapp.PADDING_PERCENT
import com.pavlig43.roofapp.SEMI_ALPHA
import com.pavlig43.roofapp.STROKE_WIDTH_MEDIUM_PDF_CANVAS
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.convertSheetDotToPx
import com.pavlig43.roofapp.model.replaceX
import com.pavlig43.roofapp.model.withOStartOffset
import com.pavlig43.roofapp.utils.canvasDrawUtils.drawSheet
import com.pavlig43.roofapp.utils.canvasDrawUtils.rulerOnCanvasPDF
import com.pavlig43.roofapp.utils.searchDotsSheet
import com.pavlig43.roofapp.utils.searchInterpolation
import com.pavlig43.roofapp.utils.toOffset
import java.math.BigDecimal
import java.math.RoundingMode

@Suppress("DestructuringDeclarationWithTooManyEntries")
class QuadroPDF(
    geometry4SideShape: Geometry4SideShape,
    val sheet: Sheet,
) {
    private val widthPage = A4WIDTH
    private val heightPage = A4HEIGHT
    private val paddingWidth = (widthPage * PADDING_PERCENT).toFloat()
    private val paddingHeight = (heightPage * PADDING_PERCENT).toFloat()

    private val listOfSheets: MutableList<Sheet> = mutableListOf()

    /**
     * из 4 точек 4хугольника вычисляю минимальные значения точек , которые будут мспользованны
     * для поиска начала координат
     */

    val sOx = arrayOf(
        geometry4SideShape.leftBottom,
        geometry4SideShape.leftTop,
        geometry4SideShape.rightTop,
        geometry4SideShape.rightBottom,
    ).minOf { it.offset.x }

    val sOy = arrayOf(
        geometry4SideShape.leftBottom,
        geometry4SideShape.leftTop,
        geometry4SideShape.rightTop,
        geometry4SideShape.rightBottom,
    ).minOf { it.offset.y }
    private val startOffset: OffsetBD =
        OffsetBD(sOx, sOy)

    private val a = geometry4SideShape.leftBottom.withOStartOffset(startOffset)
    private val b = geometry4SideShape.leftTop.withOStartOffset(startOffset)
    private val c = geometry4SideShape.rightTop.withOStartOffset(startOffset)
    private val d = geometry4SideShape.rightBottom.withOStartOffset(startOffset)
    

    /**
     * самая большая величина координаты высоты фигуры
     */
    private val peakXMax = listOf(a, b, c, d).maxOf { it.offset.x }

    /**
     * самая маленькая величина координаты высоты фигуры(низшая точка)
     */
    private val peakXMin = listOf(a, b, c, d).minOf { it.offset.x }

    private val leftSide = a.offset.getSide(b.offset)
    private val bottomSide = a.offset.getSide(d.offset)
    private val topSide = b.offset.getSide(c.offset)
    private val rightSide = c.offset.getSide(d.offset)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = maxOf(c.offset.y, d.offset.y)

    private val countCeilCMWidth =
        maxWidthShape.divide(BigDecimal("100"), RoundingMode.CEILING).times(BigDecimal("100"))
            .toInt()

    /**
     * число листов , которое будет положено по ширине фигуре, пока листы не закроют ее полностью
     */
    private val countOfSheet: Int =
        (maxWidthShape - sheet.overlap.value).divide(sheet.visible, RoundingMode.CEILING).toInt()

    /**
     * Высота фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxHeightShape = maxOf(b.offset.x, c.offset.x)

    private val countCeilCMHeight =
        maxHeightShape.divide(BigDecimal("100"), RoundingMode.CEILING).times(BigDecimal("100"))
            .toInt()

    private fun getCountPXinOneCM(
        sizeSidePage: Int,
        pagePadding: Float,
        countCeilCM: Int,
    ): BigDecimal =
        ((sizeSidePage - pagePadding * 2) / countCeilCM).toBigDecimal()

    private val oneCMInHeightYtPx: BigDecimal =
        getCountPXinOneCM(heightPage, paddingHeight, countCeilCMWidth)

    private val oneCMInWidthXPx: BigDecimal =
        getCountPXinOneCM(widthPage, paddingWidth, countCeilCMHeight)

    fun ruler(canvas: Canvas) {
        rulerOnCanvasPDF(
            canvas,
            countCeilCMWidth = countCeilCMWidth,
            countCeilCMHeight = countCeilCMHeight,
            oneCMInHeightYtPx = oneCMInHeightYtPx.toFloat(),
            oneCMInWidthXPx = oneCMInWidthXPx.toFloat(),
            paddingWidth = paddingWidth,
            paddingHeight = paddingHeight
        )
    }

    fun drawQuadro(
        canvas: Canvas,
        paint: Paint =
            Paint().apply {
                color = Color.BLACK
                strokeWidth = STROKE_WIDTH_MEDIUM_PDF_CANVAS
                alpha = SEMI_ALPHA
                style = Paint.Style.STROKE
            },
    ) {

        val (aPX, bPX, cPX, dPX) =
            listOf(a, b, c, d).map {
                it.offset
                    .changeOffset(
                        oneUnitInHeightYtPx = oneCMInHeightYtPx,
                        oneUnitInWidthXPx = oneCMInWidthXPx,
                    ).toOffset()
            }

        val path =
            android.graphics.Path().apply {
                moveTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
                lineTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
                moveTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
                lineTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
                moveTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
                lineTo(dPX.x + paddingWidth, dPX.y + paddingHeight)
                moveTo(dPX.x + paddingWidth, dPX.y + paddingHeight)
                lineTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
                close()
            }
        canvas.drawPath(path, paint)
    }

    /**
     * Выдает точки пересечения вертикальной стороны листа железа с каждой из сторон 4х-угольника
     * Если 4х-угольник правильный , то каждая вертикальная сторона листа имеет МАКСИМУМ
     * пересечений с 2 сторонами 4х-угольника
     * Если пересечение попадает на угол фигуры, то обе точки пересечения с фигурой одинаковые
     * Если пересечение отсутствует(в случае , когда правая сторона  последнего листа выходит за
     * пределы фигуры)
     * то "Х" берется от левой стороны этого же листа [lastNotNullBottomX] и [lastNotNullTopX]
     */
    private fun searchLineOfSheet(
        y: BigDecimal,
        lastNotNullBottomX: BigDecimal = BigDecimal.ZERO,
        lastNotNullTopX: BigDecimal = BigDecimal.ZERO,
    ): Result<Pair<OffsetBD, OffsetBD>> {
        val intersectAB = searchInterpolation(a, b, y, a.offset.x)
        val intersectBC = searchInterpolation(b, c, y, b.offset.x)
        val intersectDC = searchInterpolation(d, c, y, d.offset.x)
        val intersectAD = searchInterpolation(a, d, y, a.offset.x)
        val lst =
            setOfNotNull(
                intersectAB,
                intersectBC,
                intersectDC,
                intersectAD,
            ).toList()
        Log.d("intersect", "$y\nab+$intersectAB\nbc+$intersectBC\ndc+$intersectDC\nad+$intersectAD")
        Log.d("intersectLSt", lst.toString())
        return when (lst.size) {
            2 -> Result.success(Pair(lst[0], lst[1]))
            1 -> Result.success(Pair(lst[0], lst[0]))
            0 ->
                Result.success(
                    Pair(
                        OffsetBD(lastNotNullBottomX, y),
                        OffsetBD(lastNotNullTopX, y),
                    ),
                )

            else -> Result.failure(IllegalStateException("Невозможно найти 2 пересечения"))
        }
    }

    /**
     * ищем последовательность "У" при котором "Х" находится на пике(минимальном или максимальном)
     */
    private fun findYPeakX(peak: BigDecimal): ClosedRange<BigDecimal> {
        val peakDots =
            listOf(a, b, c, d)
                .asSequence()
                .filter { it.offset.x == peak }
                .sortedBy { it.offset.y }
                .take(2)
                .map { it.offset.y }
                .toList()
        return when (peakDots.size) {
            1 -> peakDots[0]..peakDots[0]
            2 -> peakDots[0]..peakDots[1]
            else -> BigDecimal.ZERO..BigDecimal.ZERO
        }
    }

    fun sheetOnQuadro(canvas: Canvas) {
        val intervalYForXMax = findYPeakX(peakXMax)
        val intervalYForXMin = findYPeakX(peakXMin)
        for (s in 1..countOfSheet) {
            val y = sheet.visible * (s - 1).toBigDecimal()
            val resultLeft = searchLineOfSheet(y)
            val resultRight =
                searchLineOfSheet(
                    y = y + sheet.widthGeneral.value,
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
            val searchDotSheet = searchDotsSheet(resultLeft, resultRight)
            Log.d("searchDotSheet", searchDotSheet.toString())
            if (dotsOfSheet != null) {
                val sheetMultiplicityCM = sheet.multiplicity
                val lenOfSheetInCM = dotsOfSheet.leftTop.x - dotsOfSheet.leftBottom.x
                val lenOfSheet =
                    lenOfSheetInCM
                        .divide(sheet.multiplicity.value, RoundingMode.CEILING)
                        .multiply(sheetMultiplicityCM.value)

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

    fun getOtherParams(): List<Pair<String, String>> =
        listOf(
            Pair("AB", "$leftSide cm"),
            Pair("BC", "$topSide cm"),
            Pair("CD", "$rightSide cm"),
            Pair("AD", "$bottomSide cm"),
            Pair("Высота фигуры ", "${maxHeightShape.toInt()} cm"),
            Pair("Ширина фигуры ", "${maxWidthShape.toInt()} cm"),
        )
}
