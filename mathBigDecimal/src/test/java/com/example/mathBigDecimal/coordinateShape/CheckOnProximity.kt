package com.example.mathBigDecimal.coordinateShape

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CheckOnProximity {
    @Test
    fun `check On Proximity With polygon ((0, 0), (50, 0), (1, 1) and Radius dot 20`() {
        val polygon = CoordinateShape(
            listOf(
                OffsetBD.Zero,
                OffsetBD(BigDecimal("50"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("1"), BigDecimal("1"))
            )
        )
        val result1 = polygon.checkOnProximity(
            OffsetBD(BigDecimal("1"), BigDecimal("1")),
            radiusOfDot = 20f
        )
        val result2 = polygon.checkOnProximity(
            OffsetBD(BigDecimal("3"), BigDecimal("3")),
            radiusOfDot = 20f
        )
        assertTrue(result1)
        assertTrue(result2)
    }
}
