package com.pavlig43.roofapp.model

import com.example.mathbigdecimal.Trapezoid
import com.example.mathbigdecimal.utils.acos
import com.example.mathbigdecimal.utils.atan
import com.example.mathbigdecimal.utils.cos
import com.example.mathbigdecimal.utils.hypot
import com.example.mathbigdecimal.utils.tan
import com.example.mathbigdecimal.utils.toDegrees
import com.example.mathbigdecimal.utils.toRadians
import java.math.BigDecimal
import java.math.MathContext

/**
 * класс для орределения 4хскатной крыши
 */
data class RoofParamsClassic4Scat(
    val width: BigDecimal = BigDecimal(1000),
    val len: BigDecimal = BigDecimal(1100),
    // угол наклона листа
    val angle: BigDecimal = BigDecimal.ZERO,
    val height: BigDecimal = BigDecimal.ZERO,
    // Покат
    val hypotenuse: BigDecimal = BigDecimal(600),
    val sheet: Sheet = Sheet(),
) {
    val yandova: BigDecimal by lazy {
        hypot(width.divide(BigDecimal(2)), hypotenuse)
    }

    /**
     * Длина конька - верхнего основания трапеции
     */
    val smallFoot: BigDecimal by lazy {

        Trapezoid.smallFoot(
            bigFoot = len,
            height = hypotenuse,
            edge = yandova,
        )
    }
}

/**
 * Пересчитывается высота,покат исходя из угла наклона
 */
fun RoofParamsClassic4Scat.calculateFromAngle(angle: BigDecimal): RoofParamsClassic4Scat {
    return when {
        angle == BigDecimal.ZERO -> this.zeroRoofParamsClassic4Scat()

        else -> {
            val adjacent = width.divide(BigDecimal(2)) // длина прилежащего катета
            val angleInRadians = toRadians(angle)
            val height = adjacent * tan(angleInRadians)
            val hypotenuse = adjacent / cos(angleInRadians)
            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
    }
}

/**
 * Пересчитывается угол и покат исходя из высоты крыши
 */
fun RoofParamsClassic4Scat.calculateFromHeight(newHeight: BigDecimal): RoofParamsClassic4Scat {
    return when {
        newHeight == BigDecimal.ZERO -> this.zeroRoofParamsClassic4Scat()
        else -> {
            val adjacent = width.divide(BigDecimal(2))
            val hypotenuse = hypot(adjacent, newHeight)

            val angleInRadians = atan(newHeight / adjacent)

            val angle = toDegrees(angleInRadians)
            this.copy(height = newHeight, angle = angle, hypotenuse = hypotenuse)
        }
    }
}

/**
 * Пересчитывает угол наклона и высоту исходя из поката
 */
fun RoofParamsClassic4Scat.calculateFromHypotenuse(hypotenuse: BigDecimal): RoofParamsClassic4Scat {
    return when {
        hypotenuse == BigDecimal.ZERO -> this.zeroRoofParamsClassic4Scat()

        else -> {
            val adjacent = width.divide(BigDecimal(2)) // прилежащий катет
            val angleInRadians =
                acos(adjacent.divide(hypotenuse, MathContext(10)))
                    ?: return this.copy(hypotenuse = hypotenuse, angle = BigDecimal.ZERO, height = BigDecimal.ZERO)
            val angle = toDegrees(angleInRadians)
            val height = adjacent * tan(angleInRadians)

            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
    }
}

fun RoofParamsClassic4Scat.zeroRoofParamsClassic4Scat() =
    this.copy(
        height = BigDecimal.ZERO,
        angle = BigDecimal.ZERO,
        hypotenuse = BigDecimal.ZERO,
    )
