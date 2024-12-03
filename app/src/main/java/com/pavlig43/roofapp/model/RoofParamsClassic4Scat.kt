package com.pavlig43.roofapp.model

import com.pavlig43.mathbigdecimal.shapes.RightTriangle
import com.pavlig43.mathbigdecimal.shapes.Trapezoid
import com.pavlig43.mathbigdecimal.utils.hypot
import com.pavlig43.roof_app.R
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * класс для орределения 4хскатной крыши
 */
data class RoofParamsClassic4Scat(
    val width: RoofParam = RoofParam(RoofParamName.WIDTH, value = BigDecimal("1000")),
    val len: RoofParam = RoofParam(RoofParamName.LEN, value = BigDecimal("1100")),
    // угол наклона листа
    val angle: RoofParam = RoofParam(RoofParamName.ANGLE, unit = UnitOfMeasurement.ANGLE),
    val height: RoofParam = RoofParam(RoofParamName.HEIGHT),
    // Покат
    val pokat: RoofParam = RoofParam(RoofParamName.POKAT),

) {
    val yandova: RoofParam by lazy {
        RoofParam(
            RoofParamName.YANDOVA,
            hypot(width.value.divide(BigDecimal(2)), pokat.value)
        )
    }

    /**
     * Длина конька - верхнего основания трапеции
     */
    val smallFoot: RoofParam by lazy {
        RoofParam(
            RoofParamName.SMALL_FOOT,
            Trapezoid.smallFoot(
                bigFoot = len.value,
                height = pokat.value,
                edge = yandova.value,
            )
        )
    }
}

enum class RoofParamName(
    val title: Int,
) {
    WIDTH(R.string.width_roof),
    LEN(R.string.len_roof),
    ANGLE(R.string.angle_tilt),
    HEIGHT(R.string.height_roof),
    POKAT(R.string.pokat),
    SMALL_FOOT(R.string.small_foot),
    YANDOVA(R.string.yandova)
}

data class RoofParam(
    val name: RoofParamName,
    val value: BigDecimal = BigDecimal.ZERO,
    val unit: UnitOfMeasurement = UnitOfMeasurement.CM,
)

fun RoofParamsClassic4Scat.updateRoofParams(roofParam: RoofParam): RoofParamsClassic4Scat =
    when (roofParam.name) {
        RoofParamName.WIDTH -> this.copy(width = roofParam)
        RoofParamName.LEN -> this.copy(len = roofParam)
        RoofParamName.ANGLE -> this.calculateFromAngle(roofParam)
        RoofParamName.HEIGHT -> this.calculateFromHeight(roofParam)
        RoofParamName.POKAT -> this.calculateFromPokat(roofParam)
        RoofParamName.SMALL_FOOT -> TODO()
        RoofParamName.YANDOVA -> TODO()
    }

/**
 * Пересчитывается высота,покат исходя из угла наклона
 */
private fun RoofParamsClassic4Scat.calculateFromAngle(angle: RoofParam): RoofParamsClassic4Scat =
    when {
        angle.value == BigDecimal.ZERO -> this.zeroParam()

        else -> {
            val adjacent = width.value.divide(BigDecimal(2)) // длина прилежащего катета
            val height =
                RightTriangle.oppositeFromAngleAndAdjacent(adjacent, angle.value)
                    .setScale(2, RoundingMode.HALF_UP)
            val pokat =
                RightTriangle.hypotenuseFromAngleAndAdjacent(adjacent, angle.value)
                    .setScale(2, RoundingMode.HALF_UP)
            this.copy(
                angle = angle,
                height = this.height.copy(value = height),
                pokat = this.pokat.copy(value = pokat),
            )
        }
    }

/**
 * Пересчитывается угол и покат исходя из высоты крыши
 */
private fun RoofParamsClassic4Scat.calculateFromHeight(newHeight: RoofParam): RoofParamsClassic4Scat =
    when {
        newHeight.value == BigDecimal.ZERO -> this.zeroParam()
        else -> {
            val adjacent = width.value.divide(BigDecimal(2))
            val pokat = hypot(adjacent, newHeight.value).setScale(2, RoundingMode.HALF_UP)

            val angle =
                RightTriangle.angleFromOppositeAndAdjacent(newHeight.value, adjacent)
                    .setScale(2, RoundingMode.HALF_UP)
            this.copy(
                height = newHeight,
                angle = this.angle.copy(value = angle),
                pokat = this.pokat.copy(value = pokat),
            )
        }
    }

/**
 * Пересчитывает угол наклона и высоту исходя из поката
 */
private fun RoofParamsClassic4Scat.calculateFromPokat(pokat: RoofParam): RoofParamsClassic4Scat {
    return when {
        pokat.value == BigDecimal.ZERO -> RoofParamsClassic4Scat()

        else -> {
            val adjacent = width.value.divide(BigDecimal(2)) // прилежащий катет
            val angle =
                RightTriangle.angleFromAdjacentAndHypotenuse(adjacent, pokat.value)
                    ?.setScale(2, RoundingMode.HALF_UP)
                    ?: BigDecimal.ZERO

            val height = RightTriangle.oppositeFromAngleAndAdjacent(adjacent, angle)
                .setScale(2, RoundingMode.HALF_UP)

            this.copy(
                angle = this.angle.copy(value = angle),
                height = this.height.copy(value = height),
                pokat = pokat,
            )
        }
    }
}

private fun RoofParamsClassic4Scat.zeroParam(): RoofParamsClassic4Scat =
    this.copy(
        pokat = pokat.copy(value = BigDecimal.ZERO),
        angle = angle.copy(value = BigDecimal.ZERO),
        height = height.copy(value = BigDecimal.ZERO),
    )
