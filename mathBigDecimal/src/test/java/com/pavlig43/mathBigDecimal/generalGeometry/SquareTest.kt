package com.pavlig43.mathBigDecimal.generalGeometry

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.shapes.staticShapes.Square
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SquareTest {
    @Test
    fun `square test with 4 side`() {
        val a = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("4"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("4"), BigDecimal("4")),
            OffsetBD(BigDecimal.ZERO, BigDecimal("4"))
        )
        val square = Square(a)
        assertEquals(0, BigDecimal("16").compareTo(square.area()))
        assertEquals(0, BigDecimal("16").compareTo(square.perimeter()))
    }
}
