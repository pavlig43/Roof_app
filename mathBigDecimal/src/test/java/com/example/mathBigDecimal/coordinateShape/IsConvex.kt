package com.example.mathBigDecimal.coordinateShape

import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class IsConvex {
    @Test
    fun testConvexTriangle() {
        val polygon = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("1"), BigDecimal.ZERO),
            OffsetBD(BigDecimal.ZERO, BigDecimal("1"))
        )
        val shape = CoordinateShape(polygon)
        assertTrue(shape.isConvex)
    }

    // Выпуклый четырехугольник
    @Test
    fun testConvexQuadrilateral() {
        val polygon = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("2"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("2"), BigDecimal("2")),
            OffsetBD(BigDecimal.ZERO, BigDecimal("2"))
        )
        val shape = CoordinateShape(polygon)
        assertTrue(shape.isConvex)
    }

    // Невыпуклый четырехугольник
    @Test
    fun testConcaveQuadrilateral() {
        val polygon = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            OffsetBD(BigDecimal("2"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("1"), BigDecimal("1")),
            OffsetBD(BigDecimal.ZERO, BigDecimal("2"))
        )
        val shape = CoordinateShape(polygon)
        assertTrue(shape.isConvex)
    }

    // Пентаграмма (звезда)
    @Test
    fun testPentagram() {
        val pentagram = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal("1")),
            OffsetBD(BigDecimal("0.9511"), BigDecimal("0.309")),
            OffsetBD(BigDecimal("0.5878"), BigDecimal("-0.809")),
            OffsetBD(BigDecimal("-0.5878"), BigDecimal("-0.809")),
            OffsetBD(BigDecimal("-0.9511"), BigDecimal("0.309"))
        )
        val shape = CoordinateShape(pentagram)
        assertTrue(shape.isConvex)
    }

    // Выпуклый пятиугольник
    @Test
    fun testConvexPentagon() {
        val pentagon = listOf(
            OffsetBD(BigDecimal.ZERO, BigDecimal("1")),
            OffsetBD(BigDecimal("1"), BigDecimal("0.5")),
            OffsetBD(BigDecimal("0.8"), BigDecimal("-1")),
            OffsetBD(BigDecimal("-0.8"), BigDecimal("-1")),
            OffsetBD(BigDecimal("-1"), BigDecimal("0.5"))
        )
        val shape = CoordinateShape(pentagon)
        assertTrue(shape.isConvex)
    }

    // degeneratePentagon
    @Test
    fun testConvexDegeneratePolygon() {
        val degeneratePentagon = listOf(
            OffsetBD(BigDecimal("-2"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("-1"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("0"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("1"), BigDecimal.ZERO),
            OffsetBD(BigDecimal("2"), BigDecimal.ZERO)
        )
        val shape = CoordinateShape(degeneratePentagon)
        assertTrue(shape.isConvex)
    }
}
