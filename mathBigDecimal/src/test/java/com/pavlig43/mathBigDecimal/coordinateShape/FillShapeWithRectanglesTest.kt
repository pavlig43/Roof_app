package com.pavlig43.mathBigDecimal.coordinateShape

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.mathbigdecimal.shapes.RightRectangle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FillShapeWithRectanglesTest {
    @Suppress("LongMethod")
    @Test
    fun `fillShapeWith Trapezoid Rectangles With RectangleWidth = 118 And Overlap = 8`() {
        val coordinateShape =
            CoordinateShape(
                basicPolygon =
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                    OffsetBD(BigDecimal("600"), BigDecimal("500")),
                    OffsetBD(BigDecimal("600"), BigDecimal("600")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("1100")),
                ),
            )

        val expected =
            listOf(
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                        OffsetBD(BigDecimal("141.6"), BigDecimal.ZERO),
                        OffsetBD(BigDecimal("141.6"), BigDecimal("118")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("118")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("110")),
                        OffsetBD(BigDecimal("273.6"), BigDecimal("110")),
                        OffsetBD(BigDecimal("273.6"), BigDecimal("228")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("228")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("220")),
                        OffsetBD(BigDecimal("405.6"), BigDecimal("220")),
                        OffsetBD(BigDecimal("405.6"), BigDecimal("338")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("338")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("330")),
                        OffsetBD(BigDecimal("537.6"), BigDecimal("330")),
                        OffsetBD(BigDecimal("537.6"), BigDecimal("448")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("448")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("440")),
                        OffsetBD(BigDecimal("600"), BigDecimal("440")),
                        OffsetBD(BigDecimal("600"), BigDecimal("558")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("558")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("550")),
                        OffsetBD(BigDecimal("600"), BigDecimal("550")),
                        OffsetBD(BigDecimal("600"), BigDecimal("668")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("668")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("660")),
                        OffsetBD(BigDecimal("528"), BigDecimal("660")),
                        OffsetBD(BigDecimal("528"), BigDecimal("778")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("778")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("770")),
                        OffsetBD(BigDecimal("396"), BigDecimal("770")),
                        OffsetBD(BigDecimal("396"), BigDecimal("888")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("888")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("880")),
                        OffsetBD(BigDecimal("264"), BigDecimal("880")),
                        OffsetBD(BigDecimal("264"), BigDecimal("998")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("998")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal.ZERO, BigDecimal("990")),
                        OffsetBD(BigDecimal("132"), BigDecimal("990")),
                        OffsetBD(BigDecimal("132"), BigDecimal("1108")),
                        OffsetBD(BigDecimal.ZERO, BigDecimal("1108")),
                    ),
                ),
            )
        val actual =
            coordinateShape.fillShapeWithRectangles(
                rectangleWidth = BigDecimal("118"),
                overlap = BigDecimal("8"),
            )
        actual.zip(expected) { a, b ->

            assertEquals(0, a.compareTo(b), "\n actual: $a, \n expected: $b")
        }
    }

    @Suppress("LongMethod")
    @Test
    fun `fillShapeWith Random Quadrilateral Rectangles With RectangleWidth = 118 And Overlap = 8`() {
        val coordinateShape =
            CoordinateShape(
                basicPolygon =
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                    OffsetBD(BigDecimal("500"), BigDecimal("-200")),
                    OffsetBD(BigDecimal("400"), BigDecimal("200")),
                    OffsetBD(BigDecimal("-200"), BigDecimal("400")),
                ),
                isMoveToPositiveQuadrant = true,
            )
        val expected =
            listOf(
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("405"), BigDecimal.ZERO),
                        OffsetBD(BigDecimal("700"), BigDecimal.ZERO),
                        OffsetBD(BigDecimal("700"), BigDecimal("118")),
                        OffsetBD(BigDecimal("405"), BigDecimal("118")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("186"), BigDecimal("110")),
                        OffsetBD(BigDecimal("672.5"), BigDecimal("110")),
                        OffsetBD(BigDecimal("672.5"), BigDecimal("228")),
                        OffsetBD(BigDecimal("186"), BigDecimal("228")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("131"), BigDecimal("220")),
                        OffsetBD(BigDecimal("645"), BigDecimal("220")),
                        OffsetBD(BigDecimal("645"), BigDecimal("338")),
                        OffsetBD(BigDecimal("131"), BigDecimal("338")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("76"), BigDecimal("330")),
                        OffsetBD(BigDecimal("617.5"), BigDecimal("330")),
                        OffsetBD(BigDecimal("617.5"), BigDecimal("448")),
                        OffsetBD(BigDecimal("76"), BigDecimal("448")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("21"), BigDecimal("440")),
                        OffsetBD(BigDecimal("480"), BigDecimal("440")),
                        OffsetBD(BigDecimal("480"), BigDecimal("558")),
                        OffsetBD(BigDecimal("21"), BigDecimal("558")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("25"), BigDecimal("550")),
                        OffsetBD(BigDecimal("150"), BigDecimal("550")),
                        OffsetBD(BigDecimal("150"), BigDecimal("668")),
                        OffsetBD(BigDecimal("25"), BigDecimal("668")),
                    ),
                ),
            )
        val actual =
            coordinateShape.fillShapeWithRectangles(
                rectangleWidth = BigDecimal("118"),
                overlap = BigDecimal("8"),
            )
        actual.zip(expected) { a, b ->

            assertEquals(0, a.compareTo(b), "\n actual: $a, \n expected: $b")
        }
    }

    @Suppress("LongMethod")
    @Test
    fun `fillShapeWith Random Triangle  With RectangleWidth = 118 And Overlap = 8`() {
        val coordinateShape =
            CoordinateShape(
                basicPolygon =
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                    OffsetBD(BigDecimal("500"), BigDecimal("-200")),
                    OffsetBD(BigDecimal("-200"), BigDecimal("300")),
                ),
                isMoveToPositiveQuadrant = true,
            )
        val expected =
            listOf(
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("405.000"), BigDecimal.ZERO),
                        OffsetBD(BigDecimal("700.000"), BigDecimal.ZERO),
                        OffsetBD(BigDecimal("700.000"), BigDecimal("118")),
                        OffsetBD(BigDecimal("405.000"), BigDecimal("118")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("181.400"), BigDecimal("110")),
                        OffsetBD(BigDecimal("546.000"), BigDecimal("110")),
                        OffsetBD(BigDecimal("546.000"), BigDecimal("228")),
                        OffsetBD(BigDecimal("181.400"), BigDecimal("228")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("108.000"), BigDecimal("220")),
                        OffsetBD(BigDecimal("392.000"), BigDecimal("220")),
                        OffsetBD(BigDecimal("392.000"), BigDecimal("338")),
                        OffsetBD(BigDecimal("108.000"), BigDecimal("338")),
                    ),
                ),
                RightRectangle(
                    listOf(
                        OffsetBD(BigDecimal("34.600"), BigDecimal("330")),
                        OffsetBD(BigDecimal("238.000"), BigDecimal("330")),
                        OffsetBD(BigDecimal("238.000"), BigDecimal("448")),
                        OffsetBD(BigDecimal("34.600"), BigDecimal("448")),
                    ),
                ),
            )

        val actual =
            coordinateShape.fillShapeWithRectangles(
                rectangleWidth = BigDecimal("118"),
                overlap = BigDecimal("8"),
            )
        actual.zip(expected) { a, b ->

            assertEquals(0, a.compareTo(b), "\n actual: $a, \n expected: $b")
        }
    }
}
