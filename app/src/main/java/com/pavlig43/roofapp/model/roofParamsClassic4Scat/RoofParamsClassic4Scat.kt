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
    val pokat: RoofParam = RoofParam(RoofParamName.POKAT),
    val roofType: RoofType = RoofType.None

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
