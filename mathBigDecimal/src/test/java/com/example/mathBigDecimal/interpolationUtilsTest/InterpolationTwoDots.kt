package com.example.mathBigDecimal.interpolationUtilsTest

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.utils.searchInterpolation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InterpolationTwoDots {
    @Test
    fun `test interpolation with normal points`() {
        val first = OffsetBD(BigDecimal("2"), BigDecimal("3"))
        val second = OffsetBD(BigDecimal("6"), BigDecimal("7"))
        val y = BigDecimal("5")

        val result = searchInterpolation(first, second, y)

        assertNotNull(result)
        assertEquals(0, BigDecimal("4").compareTo(result?.x))


    }

    @Test
    fun `test interpolation with equal y values`() {
        val first = OffsetBD(BigDecimal("2"), BigDecimal("5"))
        val second = OffsetBD(BigDecimal("6"), BigDecimal("5"))
        val y = BigDecimal("5")

        val result = searchInterpolation(first, second, y)

        assertNotNull(result)
        assertEquals(0, BigDecimal("6").compareTo(result?.x))


    }

    @Test
    fun `test interpolation out of range`() {
        val first = OffsetBD(BigDecimal("2"), BigDecimal("3"))
        val second = OffsetBD(BigDecimal("6"), BigDecimal("7"))
        val y = BigDecimal("8")

        val result = searchInterpolation(first, second, y)

        assertNull(result)
    }

    @Test
    fun `test interpolation with period result`() {
        val first = OffsetBD(BigDecimal("1"), BigDecimal("1"))
        val second = OffsetBD(BigDecimal("9"), BigDecimal("2"))
        val y = BigDecimal("1.5")

        val result = searchInterpolation(first, second, y)

        assertNotNull(result)
        assertEquals(
            0, BigDecimal("5").compareTo(result?.x)

        )

    }

    @Test
    fun `test interpolation with y = 1_18`() {
        val first = OffsetBD(BigDecimal("1"), BigDecimal("1"))
        val second = OffsetBD(BigDecimal("5"), BigDecimal("2"))
        val y = BigDecimal("1.18")

        val result = searchInterpolation(first, second, y)

        assertNotNull(result)
        assertEquals(
            BigDecimal("1.720"),
            result?.x
        )
        assertEquals(BigDecimal("1.18"), result?.y)
    }
}
