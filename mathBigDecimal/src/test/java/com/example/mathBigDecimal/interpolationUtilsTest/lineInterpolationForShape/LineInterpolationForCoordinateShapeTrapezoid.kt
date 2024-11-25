package com.example.mathBigDecimal.interpolationUtilsTest.lineInterpolationForShape

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.utils.lineInterpolationForShape
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class LineInterpolationForCoordinateShapeTrapezoid {
    private val point1 = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO)
    private val point2 = OffsetBD(BigDecimal("600"), BigDecimal("500"))
    private val point3 = OffsetBD(BigDecimal("600"), BigDecimal("600"))
    private val point4 = OffsetBD(BigDecimal.ZERO, BigDecimal("1100"))
    private val trapezoid = listOf(point1, point2, point3, point4)

    private fun withYEquals(
        y: BigDecimal,
        expected: Set<OffsetBD>,
    ) {
        val result = trapezoid.lineInterpolationForShape(y)
        result.zip(expected) { a, b -> assertEquals(0, a.compareTo(b)) }
    }

    @Suppress("LongMethod")
    @Test
    fun testDifferentYValues() {
        val testCases =
            mapOf(
                BigDecimal.ZERO to
                    setOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                        OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                    ),
                BigDecimal("110") to
                    setOf(
                        OffsetBD(BigDecimal("132"), BigDecimal("110")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("110")),
                    ),
                BigDecimal("220") to
                    setOf(
                        OffsetBD(BigDecimal("264"), BigDecimal("220")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("220")),
                    ),
                BigDecimal("330") to
                    setOf(
                        OffsetBD(BigDecimal("396"), BigDecimal("330")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("330")),
                    ),
                BigDecimal("440") to
                    setOf(
                        OffsetBD(BigDecimal("528"), BigDecimal("440")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("440")),
                    ),
                BigDecimal("550") to
                    setOf(
                        OffsetBD(BigDecimal("600"), BigDecimal("550")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("550")),
                    ),
                BigDecimal("660") to
                    setOf(
                        OffsetBD(BigDecimal("528"), BigDecimal("660")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("660")),
                    ),
                BigDecimal("770") to
                    setOf(
                        OffsetBD(BigDecimal("396"), BigDecimal("770")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("770")),
                    ),
                BigDecimal("880") to
                    setOf(
                        OffsetBD(BigDecimal("264"), BigDecimal("880")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("880")),
                    ),
                BigDecimal("990") to
                    setOf(
                        OffsetBD(BigDecimal("132"), BigDecimal("990")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("990")),
                    ),
                BigDecimal("118") to
                    setOf(
                        OffsetBD(BigDecimal("142"), BigDecimal("118")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("118")),
                    ),
                BigDecimal("228") to
                    setOf(
                        OffsetBD(BigDecimal("274"), BigDecimal("228")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("228")),
                    ),
                BigDecimal("338") to
                    setOf(
                        OffsetBD(BigDecimal("406"), BigDecimal("338")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("338")),
                    ),
                BigDecimal("448") to
                    setOf(
                        OffsetBD(BigDecimal("538"), BigDecimal("448")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("448")),
                    ),
                BigDecimal("558") to
                    setOf(
                        OffsetBD(BigDecimal("600"), BigDecimal("558")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("558")),
                    ),
                BigDecimal("668") to
                    setOf(
                        OffsetBD(BigDecimal("518"), BigDecimal("668")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("668")),
                    ),
                BigDecimal("778") to
                    setOf(
                        OffsetBD(BigDecimal("386"), BigDecimal("778")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("778")),
                    ),
                BigDecimal("888") to
                    setOf(
                        OffsetBD(BigDecimal("254"), BigDecimal("888")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("888")),
                    ),
                BigDecimal("998") to
                    setOf(
                        OffsetBD(BigDecimal("122"), BigDecimal("998")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("998")),
                    ),
                BigDecimal("1108") to
                    setOf(
                        OffsetBD(BigDecimal("132"), BigDecimal("1108")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("1108")),
                    ),
            )
        testCases.forEach { (y, expected) ->
            withYEquals(y, expected)
        }
    }
}
