package com.pavlig43.mathbigdecimal.shapes

import com.pavlig43.mathbigdecimal.MATH_PRECISION
import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.utils.acos
import com.pavlig43.mathbigdecimal.utils.atan
import com.pavlig43.mathbigdecimal.utils.cos
import com.pavlig43.mathbigdecimal.utils.tan
import com.pavlig43.mathbigdecimal.utils.toDegrees
import com.pavlig43.mathbigdecimal.utils.toRadians
import java.math.BigDecimal
import java.math.MathContext

object Triangle {
    fun isValid(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): Boolean = a + b > c && a + c > b && c + b > a

    fun isValid(
        leftBottom: OffsetBD,
        top: OffsetBD,
        rightBottom: OffsetBD,
    ): Boolean {
        val determinant =
            (top.x - leftBottom.x) * (rightBottom.y - leftBottom.y) -
                (top.y - leftBottom.y) * (rightBottom.x - leftBottom.x)
        return determinant != BigDecimal.ZERO
    }
}

object RightTriangle {
    fun oppositeFromAngleAndAdjacent(
        adjacent: BigDecimal,
        angle: BigDecimal,
    ): BigDecimal {
        val angleInRadians = toRadians(angle)
        val opposite = adjacent * tan(angleInRadians)
        return opposite
    }

    fun hypotenuseFromAngleAndAdjacent(
        adjacent: BigDecimal,
        angle: BigDecimal,
    ): BigDecimal {
        val angleInRadians = toRadians(angle)
        val hypot = adjacent * cos(angleInRadians)
        return hypot
    }

    fun angleFromOppositeAndAdjacent(
        opposite: BigDecimal,
        adjacent: BigDecimal,
    ): BigDecimal {
        if (adjacent == BigDecimal.ZERO) return BigDecimal.ZERO

        val angleInRadians = atan(opposite / adjacent)

        val angle = toDegrees(angleInRadians)
        return angle
    }

    fun angleFromAdjacentAndHypotenuse(
        adjacent: BigDecimal,
        hypotenuse: BigDecimal,
    ): BigDecimal? {
        val angleInRadians =
            acos(adjacent.divide(hypotenuse, MathContext(MATH_PRECISION))) ?: return null
        val angle = toDegrees(angleInRadians)
        return angle
    }
}
