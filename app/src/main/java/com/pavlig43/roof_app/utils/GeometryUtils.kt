package com.pavlig43.roof_app.utils

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.SheetDots
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Ищет координату "Х" при известной "У" на отрезке между точками [first] и [second]
 * методом линейной интерполяции
 * если dotOne.distanceY == dotTwo.distanceY , то получаем, что отрезок параллелен оси "У" и координата "Х" не имеет значения TODO() нужно более аргументировано подойти к этому моменту
 * Если известный "У" находится за пределами отрезка, то вернет null
 *
 */
fun searchInterpolation(first: Dot, second: Dot, y: Float, constX: Float): Offset? {
    val (dotOne, dotTwo) = if (first.distanceY < second.distanceY) {
        Pair(first, second)
    } else {
        Pair(second, first)
    }
    when {
        dotOne.distanceY > y || dotTwo.distanceY < y -> return null
        dotOne.distanceY == dotTwo.distanceY -> return Offset(constX, y)
        else -> {
            val x =
                first.distanceX + (second.distanceX - first.distanceX) * (y - first.distanceY) / (second.distanceY - first.distanceY)
            return Offset(abs(x), y)
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
    resultLeft: Result<Pair<Offset, Offset>>,
    resultRight: Result<Pair<Offset, Offset>>,

    ): SheetDots? {

    if (resultLeft.isSuccess && resultRight.isSuccess) {
        val firstLeft = resultLeft.getOrThrow().first
        val secondLeft = resultLeft.getOrThrow().second
        val firstRight = resultRight.getOrThrow().first
        val secondRight = resultRight.getOrThrow().second
        val leftBottom = Offset(
            minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
            minOf(firstLeft.y, secondRight.y, secondLeft.y, secondRight.y)

        )
        val leftTop = Offset(
            maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
            minOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
        )
        val rightTop = Offset(
            maxOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
            maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
        )
        val rightBottom = Offset(
            minOf(firstLeft.x, secondLeft.x, secondRight.x, firstRight.x),
            maxOf(firstLeft.y, secondLeft.y, secondRight.y, secondRight.y)
        )

        val sheetDots = SheetDots(
            leftBottom = leftBottom,
            leftTop = leftTop,
            rightTop = rightTop,
            rightBottom = rightBottom,
        )
        Log.d("dotSheet", sheetDots.toString())
        return sheetDots

    } else {
        Log.d("resultRight", resultRight.exceptionOrNull().toString())
        Log.d("resultLeft", resultLeft.exceptionOrNull().toString())
        return null
    }
}

/**
 * Функция отдает расстояние между двумя точками
 */
fun getSide(first: Dot, second: Dot): Int {
    return sqrt(
        (
                ((second.distanceX - first.distanceX) * (second.distanceX - first.distanceX))
                        + ((second.distanceY - first.distanceY) * (second.distanceY - first.distanceY))).toDouble()
    ).toInt()
}

