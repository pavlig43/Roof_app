package com.pavlig43.roofapp.ui.shapes.triangle

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
import com.pavlig43.roofapp.utils.drawSheet
import com.pavlig43.roofapp.utils.getSide
import com.pavlig43.roofapp.utils.rulerOnCanvasPDF
import com.pavlig43.roofapp.utils.searchDotsSheet
import com.pavlig43.roofapp.utils.searchInterpolation
import kotlin.math.ceil

class TrianglePDF(
    geometryTriangle3SideShape: GeometryTriangle3SideShape,
    val sheet: Sheet,
) {
    private val widthPage = A4WIDTH
    private val heightPage = A4HEIGHT
    private val paddingWidth = (widthPage * 0.05).toFloat()
    private val paddingHeight = (heightPage * 0.05).toFloat()

    private val listOfSheets: MutableList<Sheet> = mutableListOf()

    private val a = geometryTriangle3SideShape.leftBottom
    private val b = geometryTriangle3SideShape.top
    private val c = geometryTriangle3SideShape.rightBottom

    /**
     * самая большая величина координаты высоты фигуры
     */
    private val peakXMax = b.distanceX

    /**
     * самая маленькая величина координаты высоты фигуры(низшая точка)
     */
    private val peakXMin = a.distanceX

    private val leftSide = getSide(a, b)
    private val bottomSide = getSide(a, c)
    private val rightSide = getSide(b, c)

    /**
     * ширина фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxWidthShape = c.distanceY

    private val countCeilMetersWidth = ceil(maxWidthShape / 100).toInt()

    /**
     * число листов , которое будет положено по ширине фигуре, пока листы не закроют ее полностью
     */
    private val countOfSheet: Int =
        ceil((maxWidthShape - sheet.overlap) / sheet.visible).toInt()

    /**
     * Высота фигуры - самая дольняя точка от нового начала координат в см
     */
    private val maxHeightShape = b.distanceX

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
        val aPX = a.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val bPX = b.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)
        val cPX = c.toPX(oneCMInHeightYtPx = oneCMInHeightYtPx, oneCMInWidthXPx = oneCMInWidthXPx)

        val path =
            android.graphics.Path().apply {
                moveTo(aPX.distanceX + paddingWidth, aPX.distanceY + paddingHeight)
                lineTo(bPX.distanceX + paddingWidth, bPX.distanceY + paddingHeight)
                moveTo(bPX.distanceX + paddingWidth, bPX.distanceY + paddingHeight)
                lineTo(cPX.distanceX + paddingWidth, cPX.distanceY + paddingHeight)
                moveTo(cPX.distanceX + paddingWidth, cPX.distanceY + paddingHeight)
                lineTo(aPX.distanceX + paddingWidth, aPX.distanceY + paddingHeight)
                close()
            }
        canvas.drawPath(path, paint)
    }

    /**
     * Выдает точки пересечения вертикальной стороны листа железа с каждой из сторон 3х-угольника
     * Если 3х-угольник правильный , то каждая вертикальная сторона листа имеет МАКСИМУМ  пересечений с 2 сторонами 3х-угольника
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
        val intersectAC = searchInterpolation(a, c, y, a.distanceX)

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
                        Offset(lastNotNullBottomX, y),
                        Offset(lastNotNullTopX, y),
                    ),
                )

            else -> Result.failure(IllegalStateException("Невозможно найти 2 пересечения"))
        }
    }

    fun sheetOnTriangle(canvas: Canvas) {
        val intervalYForXMax = b.distanceY..b.distanceY
        val intervalYForXMin = a.distanceY..a.distanceY
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

    fun getOtherParams(): List<Pair<String, String>> {
        return listOf(
            Pair("Сторона AB", "$leftSide cm"),
            Pair("Сторона BC", "$rightSide cm"),
            Pair("Сторона AC", "$bottomSide cm"),
            Pair("Высота треугольника", "${maxHeightShape.toInt()} cm"),
            Pair("Ширина треугольника", "${maxWidthShape.toInt()} cm"),
        )
    }
}
