package com.pavlig43.roofApp

import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParam
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamName
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.updateRoofParams
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RoofParamsClassic4ScatStateTest {
    private val roofParams = RoofParamsClassic4Scat()

    @Test
    fun testCalculateFromAngle() {
        val result =
            roofParams.updateRoofParams(RoofParam(RoofParamName.ANGLE, BigDecimal("33.56")))
        assertBigDecimalEquals(BigDecimal("600"), result.pokatTrapezoid.value)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle.value)
        assertBigDecimalEquals(BigDecimal("331.69"), result.height.value)
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova.value)
        assertBigDecimalEquals(BigDecimal("100.00"), result.calculateStandardRidge.value)
    }

    @Test
    fun testCalculateFromHeight() {
        val result =
            roofParams.updateRoofParams(RoofParam(RoofParamName.HEIGHT, BigDecimal("331.69")))
        assertBigDecimalEquals(BigDecimal("600"), result.pokatTrapezoid.value)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle.value, delta = BigDecimal(0.15))
        assertBigDecimalEquals(BigDecimal("331.69"), result.height.value)
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova.value)
        assertBigDecimalEquals(BigDecimal("100.00"), result.calculateStandardRidge.value)
    }

    @Test
    fun testCalculateFromPokat() {
        val result =
            roofParams.updateRoofParams(RoofParam(RoofParamName.POKAT_TRAPEZOID, BigDecimal("600")))
        assertBigDecimalEquals(BigDecimal("600"), result.pokatTrapezoid.value)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle.value)
        assertBigDecimalEquals(BigDecimal("331.69"), result.height.value, delta = BigDecimal("0.5"))
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova.value)
        assertBigDecimalEquals(BigDecimal("100.00"), result.calculateStandardRidge.value)
    }
}
