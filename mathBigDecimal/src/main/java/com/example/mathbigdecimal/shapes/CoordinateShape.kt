package com.example.mathbigdecimal.shapes

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.utils.abs
import com.example.mathbigdecimal.utils.lineInterpolationForShape
import java.math.BigDecimal
import java.math.RoundingMode

open class CoordinateShape(
    basicPolygon: List<OffsetBD>,
    isMoveToPositiveQuadrant: Boolean = false,
) {

    private val moveToPositiveQuadrantOffset = if (isMoveToPositiveQuadrant) {
        val x = basicPolygon.minOf { it.x }
        val y = basicPolygon.minOf { it.y }
        OffsetBD(x, y)
    } else {
        OffsetBD(
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )
    }
    val polygon = basicPolygon.map { it.plus(moveToPositiveQuadrantOffset.absoluteValue) }

    val peakXMax = polygon.maxOfOrNull { it.x } ?: BigDecimal.ZERO
    val peakXMin = polygon.minOfOrNull { it.x } ?: BigDecimal.ZERO

    val peakYMax = polygon.maxOfOrNull { it.y } ?: BigDecimal.ZERO
    val peakYMin = polygon.minOfOrNull { it.y } ?: BigDecimal.ZERO

    val maxDistanceY = abs(peakYMin) + abs(peakYMax)
    val maxDistanceX = abs(peakXMin) + abs(peakXMax)

    /**
     * ищем последовательность "У" при котором "Х" находится на пике(минимальном или максимальном)
     */
    private fun findRangeYPeakX(peak: BigDecimal): ClosedRange<BigDecimal> {
        val peakY =
            polygon
                .asSequence()
                .filter { it.x == peak }
                .sortedBy { it.y }
                .take(2)
                .map { it.y }
                .toList()
        return when (peakY.size) {
            1 -> peakY[0]..peakY[0]
            2 -> peakY[0]..peakY[1]
            else -> BigDecimal.ZERO..BigDecimal.ZERO
        }
    }

    val peakYRangeMin = findRangeYPeakX(peakXMin)
    val peakYRangeMax = findRangeYPeakX(peakXMax)

    private fun List<OffsetBD>.getSide(
        y: BigDecimal = BigDecimal.ZERO,
        lastNotNullBottomX: BigDecimal = BigDecimal.ZERO,
        lastNotNullTopX: BigDecimal = BigDecimal.ZERO,
    ): Pair<OffsetBD, OffsetBD> {
        val startResult = this
        return when (size) {
            2 -> Pair(startResult[0], startResult[1])
            1 -> Pair(startResult[0], startResult[0])
            0 -> Pair(
                OffsetBD(lastNotNullBottomX, y),
                OffsetBD(lastNotNullTopX, y),
            )

            else -> throw IllegalArgumentException("Expected Set size to be 0, 1, or 2, but found $size elements.")
        }
    }

    /**
     * Заполняет(закрашивает) фигуру прямоугольниками от верха до низа
     */
    fun fillShapeWithRectangles(
        rectangleWidth: BigDecimal,
        overlap: BigDecimal = BigDecimal.ZERO
    ): List<RightRectangle> {
        val rectangleVisible = rectangleWidth - overlap
        val countRectangle =
            (maxDistanceY - overlap).divide(rectangleVisible, 0, RoundingMode.CEILING).toInt()
        val listOfRectangle: MutableList<RightRectangle> = mutableListOf()

        for (s in 1..countRectangle) {
            val y = rectangleVisible * (s - 1).toBigDecimal()
            val resultLeft = polygon.lineInterpolationForShape(y).getSide()
            val resultRight = (y + rectangleWidth).run {
                polygon.lineInterpolationForShape(this)
                    .getSide(
                        this,
                        resultLeft.first.x,
                        resultLeft.second.x
                    )
            }
            val listDotsRectangleShape =
                listOf(resultLeft.first, resultLeft.second, resultRight.second, resultRight.first)
            listOfRectangle.add(RightRectangle(listDotsRectangleShape).replaceX(this))
        }
        return listOfRectangle.toList()
    }

    fun compareTo(other: CoordinateShape): Int {
        return polygon.zip(other.polygon).map { (a, b) ->

            a.compareTo(b)
        }.firstOrNull { it != 0 } ?: 0
    }

    /**
     * check polygon for convexity.
     * TODO :not work for a pentagram - view in Test
     * TODO :not work for degenerate polygon - view in Test
     *
     */
    @Suppress("ReturnCount", "MagicNumber")
    private fun inConvex(): Boolean {

        if (polygon.size < 3) return false
        var sign = 0
        for (i in polygon.indices) {
            val p0 = polygon[i]
            val p1 = polygon[(i + 1) % polygon.size]
            val p2 = polygon[(i + 2) % polygon.size]
            val pointAb = OffsetBD(p1.x - p0.x, p1.y - p0.y)
            val pointBc = OffsetBD(p2.x - p1.x, p2.y - p1.y)
            val scalar = pointAb.x * pointBc.y - pointAb.y * pointBc.x
            println("scalar $p0-$p1-$p2: $scalar")

            val currentSign = when {
                scalar > BigDecimal.ZERO -> 1
                scalar < BigDecimal.ZERO -> -1
                else -> 0
            }
            if (currentSign != 0) {
                if (sign == 0) {

                    sign = currentSign
                } else if (sign != currentSign) {
                    return false
                }
            }
        }
        return true
    }

    val isConvex = inConvex()

    override fun toString(): String = polygon.toString()
}
