package com.pavlig43.mathbigdecimal.shapes

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.TWO_PI
import com.pavlig43.mathbigdecimal.utils.abs
import com.pavlig43.mathbigdecimal.utils.atan2
import com.pavlig43.mathbigdecimal.utils.lineInterpolationForShape
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Objects
import kotlin.math.PI

open class CoordinateShape(
    basicPolygon: List<OffsetBD>,
    isMoveToPositiveQuadrant: Boolean = false,
) {
    private val moveToPositiveQuadrantOffset =
        if (isMoveToPositiveQuadrant) {
            val x = basicPolygon.minOf { it.x }
            val y = basicPolygon.minOf { it.y }
            OffsetBD(x, y)
        } else {
            OffsetBD(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
            )
        }
    val polygon = basicPolygon.map { it.plus(moveToPositiveQuadrantOffset.absoluteValue) }

    val peakXMax = polygon.maxOfOrNull { it.x } ?: BigDecimal.ZERO
    val peakXMin = polygon.minOfOrNull { it.x } ?: BigDecimal.ZERO

    val peakYMax = polygon.maxOfOrNull { it.y } ?: BigDecimal.ZERO
    val peakYMin = polygon.minOfOrNull { it.y } ?: BigDecimal.ZERO
    val width = abs(peakYMax - peakYMin)
    val height = abs(peakXMax - peakXMin)

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
            0 ->
                Pair(
                    OffsetBD(lastNotNullBottomX, y),
                    OffsetBD(lastNotNullTopX, y),
                )

            else -> throw IllegalArgumentException(
                "$this - Expected Set size to be 0, 1, or 2, but found $size elements."
            )
        }
    }

    /**
     * Заполняет(закрашивает) фигуру прямоугольниками от верха до низа
     */
    fun fillShapeWithRectangles(
        rectangleWidth: BigDecimal,
        overlap: BigDecimal = BigDecimal.ZERO,
    ): List<RightRectangle> {
        val rectangleVisible = rectangleWidth - overlap
        val countRectangle =
            (width - overlap).divide(rectangleVisible, 0, RoundingMode.CEILING).toInt()
        val listOfRectangle: MutableList<RightRectangle> = mutableListOf()

        for (s in 1..countRectangle) {
            val y = rectangleVisible * (s - 1).toBigDecimal()
            val resultLeft = polygon.lineInterpolationForShape(y).getSide()
            val resultRight =
                (y + rectangleWidth).run {
                    polygon.lineInterpolationForShape(this)
                        .getSide(
                            this,
                            resultLeft.first.x,
                            resultLeft.second.x,
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
     * Return True if the polynomial defined by the sequence of 2D
     *     points is 'strictly convex': points are valid, side lengths non-
     *     zero, interior angles are strictly between zero and a straight
     *     angle, and the polygon does not intersect itself.
     *
     *     NOTES:  1.  Algorithm: the signed changes of the direction angles
     *                 from one side to the next side must be all positive or
     *                 all negative, and their sum must equal plus-or-minus
     *                 one full turn (2 pi radians). Also check for too few,
     *                 invalid, or repeated points.
     *             2.  No check is explicitly done for zero internal angles
     *                 (180 degree direction-change angle) as this is covered
     *                 in other ways, including the `n < 3` check.
     */

    @Suppress("ReturnCount", "MagicNumber")
    private fun checkOnConvex(): Boolean {
        // Check for too few points
        if (polygon.size < 3) return false

        // Get starting information
        var oldX = polygon[polygon.size - 2].x
        var oldY = polygon[polygon.size - 2].y
        var newX = polygon[polygon.size - 1].x
        var newY = polygon[polygon.size - 1].y
        var newDirection = atan2(newY - oldY, newX - oldX)
        var angleSum = BigDecimal.ZERO
        val s = polygon + polygon.first()
        // Check each point (the side ending there, its angle) and accumulate angles
        for ((ndx, newPoint) in s.withIndex()) {
            // Update point coordinates and side directions, check side length
            val oldDirection = newDirection
            oldX = newX
            oldY = newY
            newX = newPoint.x
            newY = newPoint.y
            newDirection = atan2(newY - oldY, newX - oldX)

            if (oldX == newX && oldY == newY) return false // repeated consecutive points

            // Calculate & check the normalized direction-change angle
            var angle = newDirection - oldDirection
            if (angle <= -PI.toBigDecimal()) {
                angle += TWO_PI // make it in half-open interval (-Pi, Pi]
            } else if (angle > PI.toBigDecimal()) {
                angle -= TWO_PI
            }

            if (ndx == 0) { // if x time through loop, initialize orientation
                if (angle == BigDecimal.ZERO) return false
            } else { // if other time through loop, check orientation is stable
                // not both pos. or both neg.
                if ((angle > BigDecimal.ZERO) != (angleSum > BigDecimal.ZERO)) return false
            }

            // Accumulate the direction-change angle
            angleSum += angle
        }

        // Check that the total number of full turns is plus-or-minus 1
        return abs(angleSum / TWO_PI).toInt() == 1
    }

    val isConvex: Boolean = checkOnConvex()

    fun checkOnProximity(
        newOffsetBD: OffsetBD,
        countPxInOneCMX: BigDecimal,
        countPxInOneCMY: BigDecimal,
        startPoint: OffsetBD,
        radiusOfDot: Float,
    ): Boolean {
        val listOfDotsOnRealCanvas =
            this.polygon.map { dot ->
                val centerX = dot.x * countPxInOneCMX + startPoint.x
                val centerY = dot.y * countPxInOneCMY + startPoint.y
                OffsetBD(centerX, centerY)
            }
        val dotOnRealCanvas = OffsetBD(
            newOffsetBD.x * countPxInOneCMX + startPoint.x,
            newOffsetBD.y * countPxInOneCMY + startPoint.y
        )
        return listOfDotsOnRealCanvas.any {
            it.getDistance(dotOnRealCanvas).toFloat() < radiusOfDot * 2
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is CoordinateShape) return false
        return other.polygon == this.polygon
    }

    override fun hashCode(): Int {
        return Objects.hash(polygon)
    }

    override fun toString(): String = polygon.toString()
}
