package com.pavlig43.mathbigdecimal.utils

import com.pavlig43.mathbigdecimal.MATH_PRECISION_THREE
import com.pavlig43.mathbigdecimal.OffsetBD
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ищет координату "Х" при известной "У" на отрезке между точками [first] и [second]
 * методом линейной интерполяции
 * если dotOne.offset.y
 * == dotTwo.offset.y
 * , то получаем, что отрезок параллелен оси "У" и координата "Х" не имеет значения,
 * TODO() нужно более аргументировано подойти к этому моменту
 * Если известный "У" находится за пределами отрезка, то вернет null
 *
 */
fun searchInterpolation(
    first: OffsetBD,
    second: OffsetBD,
    y: BigDecimal,
    constX: BigDecimal = BigDecimal.ZERO,
): OffsetBD? {
    val (dotOne, dotTwo) =
        if (first.y < second.y) {
            Pair(first, second)
        } else {
            Pair(second, first)
        }
    return when {
        dotOne.y > y || dotTwo.y < y -> null

        dotOne.y == dotTwo.y -> OffsetBD(constX, y)

        else -> {
            val x =
                first.x + (second.x - first.x) *
                    (y - first.y).divide(
                        second.y - first.y,
                        MATH_PRECISION_THREE, RoundingMode.HALF_UP,
                    )
            OffsetBD(x, y)
        }
    }
}

fun Pair<
    Pair<OffsetBD, OffsetBD>,
    Pair<OffsetBD, OffsetBD>
    >.searchInterpolation(y: BigDecimal): Pair<OffsetBD, OffsetBD>? {
    val (firstStart, firstEnd) = first
    val (secondStart, secondEnd) = second

    fun Pair<OffsetBD, OffsetBD>.getParallelIntersection(y: BigDecimal): Pair<OffsetBD, OffsetBD> {
        val offsetBD1 = OffsetBD(minOf(first.x, second.x), y)
        val offsetBD2 = OffsetBD(maxOf(first.x, second.x), y)
        return Pair(offsetBD1, offsetBD2)
    }

    val result =
        when {
            firstStart.y == y && firstEnd.y == y -> first.getParallelIntersection(y)
            secondStart.y == y && secondEnd.y == y -> second.getParallelIntersection(y)
            else -> {
                val firstIntersection = searchInterpolation(firstStart, firstEnd, y)
                val secondIntersection = searchInterpolation(secondStart, secondEnd, y)
                if (firstIntersection != null && secondIntersection != null) {
                    Pair(firstIntersection, secondIntersection)
                } else {
                    null
                }
            }
        }
    return result
}

/**

 *        |/\
 *        +  \
 *       /|   \
 *      / |    \
 *     /__+_____\
 *        |
 * На вход
 */
fun List<OffsetBD>.lineInterpolationForShape(y: BigDecimal): List<OffsetBD> {
    return this.zipWithNextCircular { a, b -> searchInterpolation(a, b, y) }
        .filterNotNull()
        .toSet()
        .sortedBy { it.x }
}
