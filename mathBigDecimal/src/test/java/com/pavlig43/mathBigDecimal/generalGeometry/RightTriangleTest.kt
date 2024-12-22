package com.pavlig43.mathBigDecimal.generalGeometry

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.shapes.staticShapes.Triangle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RightTriangleTest {
    @Test
    fun `right triangle test with 3,4,5 sides`() {
        val a = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("3"), BigDecimal.ZERO),
            OffsetBD(BigDecimal.ZERO, BigDecimal("4")),
        )
        val triangle = Triangle(a)
        assertEquals(0, BigDecimal("6").compareTo(triangle.area()))
        assertEquals(0, BigDecimal("12").compareTo(triangle.perimeter()))
    }
}
