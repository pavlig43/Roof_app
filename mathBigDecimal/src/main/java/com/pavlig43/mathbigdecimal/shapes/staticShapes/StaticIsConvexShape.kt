package com.pavlig43.mathbigdecimal.shapes.staticShapes

import com.pavlig43.mathbigdecimal.MATH_PRECISION_THREE
import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.utils.abs
import com.pavlig43.mathbigdecimal.utils.zipWithNextCircular
import java.math.BigDecimal

/**
 * Класс для описания и получения параметров  любых выпуклых многоугольников
 */
abstract class StaticIsConvexShape {
    abstract val polygon: List<OffsetBD>

    fun perimeter(): BigDecimal {
        return polygon.zipWithNextCircular { a, b ->
            a.getDistance(b)
        }.sumOf { it }.setScale(MATH_PRECISION_THREE)
    }

    @Suppress("MagicNumber")
    fun area(): BigDecimal {
        val n = polygon.size
        if (n < 3) return BigDecimal.ZERO

        var area = BigDecimal.ZERO

        for (i in polygon.indices) {
            val j = (i + 1) % n
            area += (polygon[i].x * polygon[j].y - polygon[j].x * polygon[i].y)
        }

        return (abs(area) / BigDecimal("2")).setScale(MATH_PRECISION_THREE)
    }
}
