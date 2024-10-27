package com.example.mathbigdecimal.shapes

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.utils.abs
import com.example.mathbigdecimal.utils.lineInterpolationForShape
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.Flow

open class CoordinateShape(
    listOfBasicDots: List<OffsetBD>,
    isMoveToPositiveQuadrant: Boolean = false,
) {


    private val startOffset = if (isMoveToPositiveQuadrant) {
        val x = listOfBasicDots.minOf { it.x }
        val y = listOfBasicDots.minOf { it.y }
        OffsetBD(x, y)
    } else {
        OffsetBD(
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )
    }
    val listOfDots = listOfBasicDots.map { it.plus(startOffset.absoluteValue) }

    val peakXMax = listOfDots.maxOfOrNull { it.x } ?: BigDecimal.ZERO
    val peakXMin = listOfDots.minOfOrNull { it.x } ?: BigDecimal.ZERO

    val peakYMax = listOfDots.maxOfOrNull { it.y } ?: BigDecimal.ZERO
    val peakYMin = listOfDots.minOfOrNull { it.y } ?: BigDecimal.ZERO


    val widthShape = abs(peakYMin) + abs(peakYMax)
    val heightShape = abs(peakXMin) + abs(peakXMax)

    val countCeilCMWidth =
        widthShape.divide(BigDecimal("100"), RoundingMode.CEILING).times(BigDecimal("100"))
            .toInt()
    val countCeilCMHeight =
        heightShape.divide(BigDecimal("100"), RoundingMode.CEILING).times(BigDecimal("100"))
            .toInt()

    /**
     * ищем последовательность "У" при котором "Х" находится на пике(минимальном или максимальном)
     */
    private fun findRangeYPeakX(peak: BigDecimal): ClosedRange<BigDecimal> {
        val peakY =
            listOfDots
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
            (widthShape - overlap).divide(rectangleVisible, RoundingMode.CEILING).toInt()
        val listOfRectangle: MutableList<RightRectangle> = mutableListOf()
        val a = Flow

        for (s in 1..countRectangle) {
            val y = rectangleVisible * (s - 1).toBigDecimal()
            val resultLeft = listOfDots.lineInterpolationForShape(y).getSide()
            val resultRight = (y + rectangleWidth).run {
                listOfDots.lineInterpolationForShape(this)
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

        return listOfDots.zip(other.listOfDots).map { (a, b) ->


            a.compareTo(b)
        }.firstOrNull { it != 0 } ?: 0

    }

    override fun toString(): String = listOfDots.toString()
}

