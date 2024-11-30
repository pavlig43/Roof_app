package com.example.mathBigDecimal.coordinateShape

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CheckOnProximity {
    @Test
    fun `check On Proximity With polygon ((0, 0), (300, 300), (0, 600) and Radius dot 20`() {
        val polygon = CoordinateShape(
            listOf(
                OffsetBD.Zero,
                OffsetBD(BigDecimal("300"), BigDecimal("300")),
                OffsetBD(BigDecimal.ZERO, BigDecimal("600"))
            )
        )
        val countPxInOneCMX = BigDecimal("3")
        val countPxInOneCMY = BigDecimal("3")
        val startPoint = OffsetBD(BigDecimal("20"), BigDecimal("20"))
        val radiusOfDot = 20f
        val addedDots = mapOf(
            OffsetBD(BigDecimal("10"), BigDecimal("10")) to false,
            OffsetBD(BigDecimal("1"), BigDecimal("1")) to true,
            OffsetBD(BigDecimal("5"), BigDecimal("5")) to true,
            OffsetBD(BigDecimal("6"), BigDecimal("6")) to true,
        )
        addedDots.forEach { (offsetBD, expected) ->
            val result = polygon.checkOnProximity(
                offsetBD,
                countPxInOneCMX,
                countPxInOneCMY,
                startPoint,
                radiusOfDot
            )

            assertEquals(result, expected, "$offsetBD")
        }
    }
}
