package com.pavlig43.mathBigDecimal.interpolationUtilsTest.lineInterpolationForShape

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.utils.lineInterpolationForShape
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class LineInterpolationForCoordinateShape {
    @Test
    fun testLineInterpolationForShapeTriangle() {
        val lb = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO)
        val top = OffsetBD(BigDecimal("6"), BigDecimal("5"))
        val rb = OffsetBD(BigDecimal("0"), BigDecimal("10"))

        val shapeTriangle = listOf(lb, top, rb)

        val result = shapeTriangle.lineInterpolationForShape(BigDecimal("3"))

        val expected =
            setOf(
                OffsetBD(BigDecimal.ZERO, BigDecimal("3")),
                OffsetBD(BigDecimal("3.6"), BigDecimal("3")),
            )
        result.zip(expected) { a, b -> assertEquals(0, a.compareTo(b)) }
    }

    @Test
    fun testLineInterpolationForShapeWithQuadrilateral() {
        val lb = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO)
        val lt = OffsetBD(BigDecimal.ZERO, BigDecimal("10"))
        val rt = OffsetBD(BigDecimal("10"), BigDecimal("10"))
        val rb = OffsetBD(BigDecimal("10"), BigDecimal.ZERO)

        val shape = listOf(lb, lt, rt, rb)

        val result = shape.lineInterpolationForShape(BigDecimal("5"))

        val expected =
            setOf(
                OffsetBD(BigDecimal.ZERO, BigDecimal("5")),
                OffsetBD(BigDecimal("10"), BigDecimal("5")),
            )

        result.zip(expected) { a, b -> assertEquals(0, a.compareTo(b)) }
    }
}
