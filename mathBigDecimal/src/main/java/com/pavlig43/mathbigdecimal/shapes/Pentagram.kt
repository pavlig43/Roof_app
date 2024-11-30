package com.pavlig43.mathbigdecimal.shapes

import com.pavlig43.mathbigdecimal.OffsetBD
import java.math.BigDecimal
import kotlin.math.cos
import kotlin.math.sin

@Suppress("MagicNumber")
fun getPentagramPoints(radius: Int): List<OffsetBD> {
    val points = mutableListOf<OffsetBD>()
    val angleStep = BigDecimal("72")

    for (i in 0 until 5) {
        val angle = Math.toRadians(i * angleStep.toDouble())
        val x = (radius * cos(angle)).toBigDecimal()
        val y = (radius * sin(angle)).toBigDecimal()
        points.add(OffsetBD(x, y))
    }

    // Пентаграмма образуется соединением каждого 2-го угла
    val pentagramPoints = mutableListOf<OffsetBD>()
    for (i in 0 until 5) {
        pentagramPoints.add(points[i % 5]) // Добавляем вершину
        pentagramPoints.add(points[(i + 2) % 5]) // Добавляем вершину через одну
    }

    return pentagramPoints
}
