package com.pavlig43.roofapp.ui.shapes.triangle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import com.example.mathbigdecimal.OffsetBD
import com.pavlig43.roofapp.A4X
import com.pavlig43.roofapp.A4Y
import com.pavlig43.roofapp.PADDING_PERCENT
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.convertSheetDotToPx
import com.pavlig43.roofapp.model.replaceX
import com.pavlig43.roofapp.utils.canvasDrawUtils.drawSheet
import com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem.coordinateSystem
import com.pavlig43.roofapp.utils.searchDotsSheet
import com.pavlig43.roofapp.utils.searchInterpolation
import com.pavlig43.roofapp.utils.toOffset
import java.math.BigDecimal
import java.math.RoundingMode

class TrianglePDF(
    geometryTriangle3SideShape: GeometryTriangle3SideShape,
    val sheet: Sheet,
) {
    private val widthPage = A4X
    private val heightPage = A4Y
    private val paddingWidth = (widthPage * PADDING_PERCENT).toFloat()
    private val paddingHeight = (heightPage * PADDING_PERCENT).toFloat()

    private val listOfSheets: MutableList<Sheet> = mutableListOf()

    private val a = geometryTriangle3SideShape.leftBottom
    private val b = geometryTriangle3SideShape.top
    private val c = geometryTriangle3SideShape.rightBottom

    /**
     * самая большая величина координаты высоты фигуры
     */
    private val peakXMax = b.offset.x

    /**
     * самая маленькая величина координаты высоты фигуры(низшая точка)
     */
    private val peakXMin = a.offset.x

    private val leftSide = a.offset.getSide(b.offset)
    private val bottomSide = a.offset.getSide(c.offset)
    private val rightSide = b.offset.getSide(c.offset)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = c.offset.y

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
    private val maxHeightShape = b.offset.x

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

    fun ruler(
        canvas: Canvas,
    ) {
        canvas.coordinateSystem(
            countCMInX = maxHeightShape.toInt(),
            countCMInY = maxWidthShape.toInt(),

            startOffsetLine = Offset(PADDING_PERCENT * widthPage, PADDING_PERCENT * heightPage),
            countPxInOneCM = TODO(),
            rulerParam = TODO(),
            scaleRuler = TODO(),
        )
//        rulerOnCanvasPDF(
//            canvas,
//            countCMInX = maxHeightShape.toInt(),
//            countCMInY = maxWidthShape.toInt(),
//            countPxInOneCMY = oneCMInHeightYtPx.toFloat(),
//            oneCMInWidthXPx = oneCMInWidthXPx.toFloat(),
//            startOffsetLine = Offset(PADDING_PERCENT * widthPage, PADDING_PERCENT * heightPage),
//
//
//            )
    }

    fun drawTriangle(
        canvas: Canvas,
        paint: Paint =
            Paint().apply {
                color = Color.BLACK
                strokeWidth = 4f
                alpha = 162 // Толщина линии
                style = Paint.Style.STROKE
            },
    ) {
        val (aPX, bPX, cPX) =
            listOf(a, b, c).map {
                it.offset
                    .changeOffset(
                        oneUnitInHeightYtPx = oneCMInHeightYtPx,
                        oneUnitInWidthXPx = oneCMInWidthXPx,
                    ).toOffset()
            }

        val path =
            Path().apply {
                moveTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
                lineTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
                moveTo(bPX.x + paddingWidth, bPX.y + paddingHeight)
                lineTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
                moveTo(cPX.x + paddingWidth, cPX.y + paddingHeight)
                lineTo(aPX.x + paddingWidth, aPX.y + paddingHeight)
                close()
            }
        canvas.drawPath(path, paint)
    }

    /**
     * Выдает точки пересечения вертикальной стороны листа железа с каждой из сторон 3х-угольника
     * Если 3х-угольник правильный , то каждая вертикальная сторона листа имеет МАКСИМУМ
     * пересечений с 2 сторонами 3х-угольника
     * Если пересечение попадает на угол фигуры, то обе точки пересечения с фигурой одинаковые
     * Если пересечение отсутствует(в случае , когда правая сторона  последнего листа выходит
     * за пределы фигуры)
     * то "Х" берется от левой стороны этого же листа [lastNotNullBottomX] и [lastNotNullTopX]
     */
    private fun searchLineOfSheet(
        y: BigDecimal,
        lastNotNullBottomX: BigDecimal = BigDecimal.ZERO,
        lastNotNullTopX: BigDecimal = BigDecimal.ZERO,
    ): Result<Pair<OffsetBD, OffsetBD>> {
        val intersectAB = searchInterpolation(a, b, y, a.offset.x)
        val intersectBC = searchInterpolation(b, c, y, b.offset.x)
        val intersectAC = searchInterpolation(a, c, y, a.offset.x)

        val lst =
            setOfNotNull(
                intersectAB,
                intersectBC,
                intersectAC,
            ).toList()
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

    fun sheetOnTriangle(canvas: Canvas) {
        val intervalYForXMax = b.offset.y..b.offset.y
        val intervalYForXMin = a.offset.y..a.offset.y
        for (s in 1..countOfSheet) {
            val y = (s - 1).toBigDecimal() * sheet.visible
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
            if (dotsOfSheet != null) {
                val sheetMultiplicityCM = sheet.multiplicity
                val lenOfSheetInCM = dotsOfSheet.leftTop.x - dotsOfSheet.leftBottom.x
                val lenOfSheet =
                    lenOfSheetInCM.divide(
                        sheet.multiplicity.value,
                        RoundingMode.CEILING,
                    ) * sheetMultiplicityCM.value

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
                listOfSheets.add(sheet.copy(len = lenOfSheet))
            }
        }
    }

    fun getLstOfSheet() = listOfSheets

    fun getOtherParams(): List<Pair<String, String>> =
        listOf(
            Pair("Сторона AB", "$leftSide cm"),
            Pair("Сторона BC", "$rightSide cm"),
            Pair("Сторона AC", "$bottomSide cm"),
            Pair("Высота треугольника", "${maxHeightShape.toInt()} cm"),
            Pair("Ширина треугольника", "${maxWidthShape.toInt()} cm"),
        )
}
