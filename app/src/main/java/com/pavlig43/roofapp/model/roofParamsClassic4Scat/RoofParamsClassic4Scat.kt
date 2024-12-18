package com.pavlig43.roofapp.model.roofParamsClassic4Scat

import com.pavlig43.mathbigdecimal.shapes.Trapezoid
import com.pavlig43.mathbigdecimal.utils.hypot
import com.pavlig43.roofapp.model.UnitOfMeasurement
import java.math.BigDecimal

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
    val pokatTrapezoid: RoofParam = RoofParam(RoofParamName.POKAT_TRAPEZOID),
    val roofType: RoofType = RoofType.None,
    val userRidge: RoofParam? = null

) {
    private fun calculatePokatTriangle(): RoofParam {
        if (userRidge == null) return RoofParam(RoofParamName.POKAT_TRIANGLE, pokatTrapezoid.value)
        // Расстояние от конца конька до конца крыши по длине
        val catheter = (len.value - userRidge.value).divide(BigDecimal(2))
        val pokat = hypot(catheter, height.value)
        return RoofParam(RoofParamName.POKAT_TRIANGLE, pokat)
    }

    val pokatTriangle = calculatePokatTriangle()
    val yandova: RoofParam = RoofParam(
        RoofParamName.YANDOVA,
        hypot(width.value.divide(BigDecimal(2)), pokatTriangle.value)
    )


    /**
     * Длина конька - верхнего основания трапеции
     */
    val calculateStandardRidge: RoofParam by lazy {
        RoofParam(
            RoofParamName.RIDGE,
            Trapezoid.smallFoot(
                bigFoot = len.value,
                height = pokatTrapezoid.value,
                edge = yandova.value,
            )
        )
    }
}
