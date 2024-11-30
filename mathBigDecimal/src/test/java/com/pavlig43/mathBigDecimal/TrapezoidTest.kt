package com.pavlig43.mathBigDecimal

import com.pavlig43.mathbigdecimal.shapes.Trapezoid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TrapezoidTest {
    @Test
    fun `calculate small foot from bigfoot and two edge and height of trapezoid1`() {
        val smallFoot = Trapezoid.smallFoot(BigDecimal(502), BigDecimal(202), BigDecimal(309))

        assertEquals(BigDecimal("34.34"), smallFoot)
    }

    @Test
    fun `calculate small foot from bigfoot and two edge and height of trapezoid2`() {
        val smallFoot =
            Trapezoid.smallFoot(BigDecimal(1100), BigDecimal(600), BigDecimal("781.02496"))

        assertEquals(BigDecimal("100.00"), smallFoot)
    }
}
