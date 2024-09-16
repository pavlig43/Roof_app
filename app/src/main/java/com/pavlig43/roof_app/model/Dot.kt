package com.pavlig43.roof_app.model

import androidx.compose.ui.geometry.Offset


/**
 * класс для определения расстяния от наальной точки(я использую левую нижнюю)
 * если distanceX: Int = 0 и distanceY: Int = 0 то эта точка начальная
 */
data class Dot(
    val name: Enum<*>,
    val distanceX: Float = 0f,
    val distanceY: Float = 0f,
    val canMinusX: Boolean = false,
    val canMinusY: Boolean = false,
    val canChangeX:Boolean = true,
    val canChangeY:Boolean = true,
)

enum class DotName4Side {
    LEFTBOTTOM, LEFTTOP, RIGHTBOTTOM, RIGHTTOP;
}
enum class DotNameTriangle3Side{
    LEFTBOTTOM, TOP, RIGHTBOTTOM,
}

/**
 * После нахождения [startOffset] - минимальные значения по "х" и "у" у фигуры
 * изменяем этой функцией координаты всех точек фигуры , чтобы она сохранила пропорции в декартовой плоскости
 * Например для отрезка : начальная точка !!!Всегда!!! а(0,0) вторая точка b(-10,4)
 * [startOffset] = Offset(abs(-10),abs(0)) После этой функции получаем а(10,0) вторая точка b(0,4)
 * Лучше ставить ограничения на отрицательные координаты некоторых точек, чтобы  получать правильные многоугольники
 */
fun Dot.withOStartOffset(startOffset: Offset) = this.copy(
    distanceX = this.distanceX + startOffset.x,
    distanceY = this.distanceY + startOffset.y
)

/**
 * в точке координаты указаны в сантиметрах, переводит в пиксели для канваса
 */
 fun Dot.toPX(oneCMInWidthXPx:Float,oneCMInHeightYtPx:Float) = this.copy(
    distanceX = this.distanceX  * oneCMInWidthXPx,
    distanceY = this.distanceY  * oneCMInHeightYtPx
)

