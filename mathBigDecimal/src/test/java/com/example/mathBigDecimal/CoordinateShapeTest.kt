package com.example.mathBigDecimal

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.example.mathbigdecimal.shapes.RightRectangle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CoordinateShapeTest {
    private val coordinateShape = CoordinateShape(
        listOfBasicDots = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("600"), BigDecimal("500")),
            OffsetBD(BigDecimal("600"), BigDecimal("600")),
            OffsetBD(BigDecimal.ZERO, BigDecimal("1100")),
        )
    )

    @Suppress("LongMethod")
    @Test
    fun `fillShapeWith Rectangles With RectangleWidth = 118 And Overlap = 8`() {

        val expected = listOf(
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                    OffsetBD(BigDecimal("141.6"), BigDecimal.ZERO),
                    OffsetBD(BigDecimal("141.6"), BigDecimal("118")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("118"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("110")),
                    OffsetBD(BigDecimal("273.6"), BigDecimal("110")),
                    OffsetBD(BigDecimal("273.6"), BigDecimal("228")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("228"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("220")),
                    OffsetBD(BigDecimal("405.6"), BigDecimal("220")),
                    OffsetBD(BigDecimal("405.6"), BigDecimal("338")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("338"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("330")),
                    OffsetBD(BigDecimal("537.6"), BigDecimal("330")),
                    OffsetBD(BigDecimal("537.6"), BigDecimal("448")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("448"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("440")),
                    OffsetBD(BigDecimal("600"), BigDecimal("440")),
                    OffsetBD(BigDecimal("600"), BigDecimal("558")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("558"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("550")),
                    OffsetBD(BigDecimal("600"), BigDecimal("550")),
                    OffsetBD(BigDecimal("600"), BigDecimal("668")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("668"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("660")),
                    OffsetBD(BigDecimal("528"), BigDecimal("660")),
                    OffsetBD(BigDecimal("528"), BigDecimal("778")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("778"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("770")),
                    OffsetBD(BigDecimal("396"), BigDecimal("770")),
                    OffsetBD(BigDecimal("396"), BigDecimal("888")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("888"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("880")),
                    OffsetBD(BigDecimal("264"), BigDecimal("880")),
                    OffsetBD(BigDecimal("264"), BigDecimal("998")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("998"))
                )
            ),
            RightRectangle(
                listOf(
                    OffsetBD(BigDecimal.ZERO, BigDecimal("990")),
                    OffsetBD(BigDecimal("132"), BigDecimal("990")),
                    OffsetBD(BigDecimal("132"), BigDecimal("1108")),
                    OffsetBD(BigDecimal.ZERO, BigDecimal("1108"))
                )
            )
        )
        val actual = coordinateShape.fillShapeWithRectangles(
            rectangleWidth = BigDecimal("118"),
            overlap = BigDecimal("8")
        )
        actual.zip(expected) { a, b ->

            assertEquals(0, a.compareTo(b), "\n actual: $a, \n expected: $b")
        }


    }
}
