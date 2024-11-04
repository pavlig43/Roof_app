package com.pavlig43.roofapp.ui.shapes.quadrilateral

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.mathbigdecimal.OffsetBD
import com.example.pdfcanvasdraw.pdf.page.AndroidCanvas
import com.pavlig43.roofapp.A4X
import com.pavlig43.roofapp.A4Y
import com.pavlig43.roofapp.PADDING_PERCENT
import com.pavlig43.roofapp.SEMI_ALPHA
import com.pavlig43.roofapp.STROKE_WIDTH_MEDIUM_PDF_CANVAS
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.replaceX
import com.pavlig43.roofapp.model.withOstartPointF
import com.pavlig43.roofapp.utils.searchDotsSheet
import com.pavlig43.roofapp.utils.searchInterpolation
import java.math.BigDecimal
import java.math.RoundingMode

@Suppress("DestructuringDeclarationWithTooManyEntries")
class QuadroPDF(
    geometry4SideShape: Geometry4SideShape,
    val sheet: Sheet,
) {
    private val widthPage = A4X
    private val heightPage = A4Y
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
    ).minOf { it.PointF.x }

    val sOy = arrayOf(
        geometry4SideShape.leftBottom,
        geometry4SideShape.leftTop,
        geometry4SideShape.rightTop,
        geometry4SideShape.rightBottom,
    ).minOf { it.PointF.y }
    private val startPointF: OffsetBD =
        OffsetBD(sOx, sOy)

    private val a = geometry4SideShape.leftBottom.withOstartPointF(startPointF)
    private val b = geometry4SideShape.leftTop.withOstartPointF(startPointF)
    private val c = geometry4SideShape.rightTop.withOstartPointF(startPointF)
    private val d = geometry4SideShape.rightBottom.withOstartPointF(startPointF)

    /**
     * самая большая величина координаты высоты фигуры
     */
    private val peakXMax = listOf(a, b, c, d).maxOf { it.PointF.x }

    /**
     * самая маленькая величина координаты высоты фигуры(низшая точка)
     */
    private val peakXMin = listOf(a, b, c, d).minOf { it.PointF.x }

    private val leftSide = a.PointF.getSide(b.PointF)
    private val bottomSide = a.PointF.getSide(d.PointF)
    private val topSide = b.PointF.getSide(c.PointF)
    private val rightSide = c.PointF.getSide(d.PointF)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = maxOf(c.PointF.y, d.PointF.y)

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
    private val maxHeightShape = maxOf(b.PointF.x, c.PointF.x)

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

    fun ruler(canvas: AndroidCanvas) {
//        canvas.coordinateSystem(
//            countCMInX = maxHeightShape.toInt(),
//            countCMInY = maxWidthShape.toInt(),
//
//            startPointFLine = PointF(PADDING_PERCENT * widthPage, PADDING_PERCENT * heightPage),
//            countPxInOneCM = TODO(),
//            rulerParam = TODO(),
//            scaleRuler = TODO(),
//        )
    }

    fun drawQuadro(
        paint: Paint =
            Paint().apply {
                color = Color.BLACK
                strokeWidth = STROKE_WIDTH_MEDIUM_PDF_CANVAS
                alpha = SEMI_ALPHA
                style = Paint.Style.STROKE
            },
    ) {
//        val (aPX, bPX, cPX, dPX) =
//            listOf(a, b, c, d).map {
//                it.PointF
//                    .changePointF(
//                        oneUnitInHeightYtPx = oneCMInHeightYtPx,
//                        oneUnitInWidthXPx = oneCMInWidthXPx,
//                    ).toPointF()
//            }
//
//        val path =
//            android.graphics.Path().apply {
//                moveTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
//                lineTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
//                moveTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
//                lineTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
//                moveTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
//                lineTo(dPX.x + paddingWidth, dPX.y + paddingHeight)
//                moveTo(dPX.x + paddingWidth, dPX.y + paddingHeight)
//                lineTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
//                close()
//            }
//        canvas.drawPath(path, paint)
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
        val intersectAB = searchInterpolation(a, b, y, a.PointF.x)
        val intersectBC = searchInterpolation(b, c, y, b.PointF.x)
        val intersectDC = searchInterpolation(d, c, y, d.PointF.x)
        val intersectAD = searchInterpolation(a, d, y, a.PointF.x)
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
                .filter { it.PointF.x == peak }
                .sortedBy { it.PointF.y }
                .take(2)
                .map { it.PointF.y }
                .toList()
        return when (peakDots.size) {
            1 -> peakDots[0]..peakDots[0]
            2 -> peakDots[0]..peakDots[1]
            else -> BigDecimal.ZERO..BigDecimal.ZERO
        }
    }

    fun sheetOnQuadro() {
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
//                com.example.pdfcanvasdraw.canvasDrawUtils.drawSheet(
//                    canvas = canvas,
//                    sheetDots =
//                    dotsOfSheet
//                        .convertSheetDotToPx(
//                            oneMeterInHeightYtPx = oneCMInHeightYtPx,
//                            oneMeterInWidthXPx = oneCMInWidthXPx,
//                        ),
//                    lenOfSheet = lenOfSheet,
//                )
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
