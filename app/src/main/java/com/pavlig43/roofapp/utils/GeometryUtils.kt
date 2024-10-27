package com.pavlig43.roofapp.utils

import android.util.Log
import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.utils.abs
import com.pavlig43.roofapp.model.Dot
import com.pavlig43.roofapp.model.SheetDots
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ищет координату "Х" при известной "У" на отрезке между точками [first] и [second]
 * методом линейной интерполяции
 * если dotOne.offset.y
 * == dotTwo.offset.y
 * , то получаем, что отрезок параллелен оси "У" и координата "Х" не имеет значения
 * TODO() нужно более аргументировано подойти к этому моменту
 * Если известный "У" находится за пределами отрезка, то вернет null
 *
 */
fun searchInterpolation(
    first: Dot,
    second: Dot,
    y: BigDecimal,
    constX: BigDecimal,
): OffsetBD? {
    val (dotOne, dotTwo) =
        if (first.offset.y
            < second.offset.y
        ) {
            Pair(first, second)
        } else {
            Pair(second, first)
        }
    when {
        dotOne.offset.y
            > y ||
            dotTwo.offset.y
            < y -> return null

        dotOne.offset.y
            == dotTwo.offset.y
        -> return OffsetBD(constX, y)

        else -> {
            val x =
                first.offset.x + (second.offset.x - first.offset.x) * (
                    y - first.offset.y
                    ) / (
                    second.offset.y -
                        first.offset.y
                    )
            return OffsetBD(abs(x).setScale(0, RoundingMode.FLOOR), y)
        }
    }
}

/**
 * Получаем точки в координатах листа железа(соотвественно и размер),
 * ширина листа всегда одинаковая, поэтому "У" известен,
 * на вход получаем точки пересечения с фигурой правой и левой стороны листа
 * Для того , чтобы лист известной ширины полностью закрывал фигуру по высоте,
 * поэтому выбираем минимальные и максимальные значения от пересечения
 */
fun searchDotsSheet(
    resultLeft: Result<Pair<OffsetBD, OffsetBD>>,
    resultRight: Result<Pair<OffsetBD, OffsetBD>>,
): SheetDots? {
    if (resultLeft.isSuccess && resultRight.isSuccess) {
        val firstLeft = resultLeft.getOrThrow().first
        val secondLeft = resultLeft.getOrThrow().second
        val firstRight = resultRight.getOrThrow().first
        val secondRight = resultRight.getOrThrow().second
        val leftBottom =
            OffsetBD(
                minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                minOf(firstLeft.y, secondRight.y, secondLeft.y, secondRight.y),
            )
        val leftTop =
            OffsetBD(
                maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                minOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y),
            )
        val rightTop =
            OffsetBD(
                maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y),
            )
        val rightBottom =
            OffsetBD(
                minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
                maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y),
            )

        val sheetDots =
            SheetDots(
                leftBottom = leftBottom.changeOffset(),
                leftTop = leftTop.changeOffset(),
                rightTop = rightTop.changeOffset(),
                rightBottom = rightBottom.changeOffset(),
            )
        Log.d("dotSheet", sheetDots.toString())
        return sheetDots
    } else {
        Log.d("resultRight", resultRight.exceptionOrNull().toString())
        Log.d("resultLeft", resultLeft.exceptionOrNull().toString())
        return null
    }
}
