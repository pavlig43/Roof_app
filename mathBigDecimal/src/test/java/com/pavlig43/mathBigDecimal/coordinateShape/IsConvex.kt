package com.pavlig43.mathBigDecimal.coordinateShape

import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.shapes.shapeForDraw.CoordinateShape
import com.pavlig43.mathbigdecimal.shapes.staticShapes.getPentagramPoints
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class IsConvex {
    @Test
    fun testConvexTriangle() {
        val polygon =
            listOf(
                OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                OffsetBD(BigDecimal("1"), BigDecimal.ZERO),
                OffsetBD(BigDecimal.ZERO, BigDecimal("1")),
            )
        val shape = CoordinateShape(polygon)
        assertTrue(shape.isConvex)
    }

    // Выпуклый четырехугольник
    @Test
    fun testConvexQuadrilateral() {
        val polygon =
            listOf(
                OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                OffsetBD(BigDecimal("2"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("2"), BigDecimal("2")),
                OffsetBD(BigDecimal.ZERO, BigDecimal("2")),
            )
        val shape = CoordinateShape(polygon)
        assertTrue(shape.isConvex)
    }

    // Невыпуклый четырехугольник
    @Test
    fun testConcaveQuadrilateral() {
        val polygon =
            listOf(
                OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
                OffsetBD(BigDecimal("2"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("1"), BigDecimal("1")),
                OffsetBD(BigDecimal.ZERO, BigDecimal("2")),
            )
        val shape = CoordinateShape(polygon)
        assertFalse(shape.isConvex)
    }

    // Пентаграмма (звезда)
    @Test
    fun testPentagram() {
        val pentagram = getPentagramPoints(10)

        val shape = CoordinateShape(pentagram)
        assertFalse(shape.isConvex)
    }

    // Выпуклый пятиугольник
    @Test
    fun testConvexPentagon() {
        val pentagon = getPentagramPoints(10)

        val shape = CoordinateShape(pentagon)
        assertFalse(shape.isConvex)
    }

    // degeneratePentagon
    @Test
    fun testConvexDegeneratePolygon() {
        val degeneratePentagon =
            listOf(
                OffsetBD(BigDecimal("-2"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("-1"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("0"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("1"), BigDecimal.ZERO),
                OffsetBD(BigDecimal("2"), BigDecimal.ZERO),
            )
        val shape = CoordinateShape(degeneratePentagon)
        assertFalse(shape.isConvex)
    }
}
