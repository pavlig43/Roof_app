package com.pavlig43.roofapp.model.roofParamsClassic4Scat

import com.pavlig43.mathbigdecimal.shapes.RightTriangle
import com.pavlig43.mathbigdecimal.utils.hypot
import java.math.BigDecimal
import java.math.RoundingMode

fun RoofParamsClassic4Scat.updateRoofParams(roofParam: RoofParam): RoofParamsClassic4Scat {
    return when (roofParam.name) {
        RoofParamName.WIDTH -> this.copy(width = roofParam)
        RoofParamName.LEN -> this.copy(len = roofParam)
        RoofParamName.ANGLE -> this.calculateFromAngle(roofParam)
        RoofParamName.HEIGHT -> this.calculateFromHeight(roofParam)
        RoofParamName.POKAT -> this.calculateFromPokat(roofParam)
        RoofParamName.SMALL_FOOT -> TODO()
        RoofParamName.YANDOVA -> TODO()
    }.copy(roofType = RoofType.None)
}

fun RoofParamsClassic4Scat.calculateFromRoofType(roofType: RoofType): RoofParamsClassic4Scat {
    val angle =
        RoofParam(RoofParamName.ANGLE, roofType.angle ?: return this.copy(roofType = RoofType.None))

    return this.calculateFromAngle(angle).copy(roofType = roofType)
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
