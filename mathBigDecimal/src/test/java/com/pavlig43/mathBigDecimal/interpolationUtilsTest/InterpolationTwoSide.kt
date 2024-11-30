package com.pavlig43.mathBigDecimal.interpolationUtilsTest

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.utils.searchInterpolation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InterpolationTwoSide {
    @Test
    fun `test intersection when only first pair matches the y value`() {
        val first =
            Pair(
                OffsetBD(BigDecimal(500), BigDecimal(100)),
                OffsetBD(BigDecimal(700), BigDecimal(100)),
            )
        val second =
            Pair(
                OffsetBD(BigDecimal(500), BigDecimal(100)),
                OffsetBD(BigDecimal(500), BigDecimal(200)),
            )
        val result = Pair(first, second).searchInterpolation(BigDecimal(100))

        val expected =
            Pair(
                OffsetBD(BigDecimal("500.000"), BigDecimal("100.000")),
                OffsetBD(BigDecimal("700.100"), BigDecimal("100.000")),
            )
        assertEquals(0, result?.first?.compareTo(expected.first))
        assertEquals(0, result?.second?.compareTo(expected.second))
    }

    @Test
    fun `test intersection when only second pair matches the y value`() {
        val first =
            Pair(
                OffsetBD(BigDecimal(600), BigDecimal(300)),
                OffsetBD(BigDecimal(700), BigDecimal(100)),
            )
        val second =
            Pair(
                OffsetBD(BigDecimal(500), BigDecimal(200)),
                OffsetBD(BigDecimal(500), BigDecimal(100)),
            )
        val result = Pair(first, second).searchInterpolation(BigDecimal(150))

        val expected =
            Pair(
                OffsetBD(BigDecimal("500"), BigDecimal("150")),
                OffsetBD(BigDecimal("675"), BigDecimal("150")),
            )
        assertEquals(0, result?.first?.compareTo(expected.first))
        assertEquals(0, result?.second?.compareTo(expected.second))
    }

    @Test
    fun `test no intersection`() {
        val first =
            Pair(
                OffsetBD(BigDecimal(500), BigDecimal(100)),
                OffsetBD(BigDecimal(700), BigDecimal(100)),
            )
        val second =
            Pair(
                OffsetBD(BigDecimal(500), BigDecimal(200)),
                OffsetBD(BigDecimal(500), BigDecimal(100)),
            )
        val result = Pair(first, second).searchInterpolation(BigDecimal(150))
        assertNull(result)
    }
}
