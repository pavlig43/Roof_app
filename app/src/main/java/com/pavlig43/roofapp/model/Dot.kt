package com.pavlig43.roofapp.model

import com.example.mathbigdecimal.OffsetBD
import java.math.BigDecimal

/**
 * класс для определения расстяния от наальной точки(я использую левую нижнюю)
 * если distanceX: Int = 0 и distanceY: Int = 0 то эта точка начальная
 */
data class Dot(
    val name: Enum<*>,
    val PointF: OffsetBD = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
    val canMinusX: Boolean = false,
    val canMinusY: Boolean = false,
    val canChangeX: Boolean = true,
    val canChangeY: Boolean = true,
)

enum class DotName4Side {
    LEFTBOTTOM,
    LEFTTOP,
    RIGHTBOTTOM,
    RIGHTTOP,
}

enum class DotNameTriangle3Side {
    LEFTBOTTOM,
    TOP,
    RIGHTBOTTOM,
}

/**
 * После нахождения [startPointF] - минимальные значения по "х" и "у" у фигуры
 * изменяем этой функцией координаты всех точек фигуры , чтобы она сохранила пропорции в декартовой плоскости
 * Например для отрезка : начальная точка !!!Всегда!!! а(0,0) вторая точка b(-10,4)
 * [startPointF] = PointF(abs(-10),abs(0)) После этой функции получаем а(10,0) вторая точка b(0,4)
 * Лучше ставить ограничения на отрицательные координаты некоторых точек, чтобы  получать правильные многоугольники
 */
fun Dot.withOstartPointF(startPointF: OffsetBD) =
    this.copy(
        PointF = this.PointF.plus(startPointF.absoluteValue),
    )
