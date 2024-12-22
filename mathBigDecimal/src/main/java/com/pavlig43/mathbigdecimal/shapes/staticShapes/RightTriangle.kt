package com.pavlig43.mathbigdecimal.shapes.staticShapes

import com.pavlig43.mathbigdecimal.MATH_PRECISION_THREE
import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.utils.acos
import com.pavlig43.mathbigdecimal.utils.atan
import com.pavlig43.mathbigdecimal.utils.cos
import com.pavlig43.mathbigdecimal.utils.tan
import com.pavlig43.mathbigdecimal.utils.toDegrees
import com.pavlig43.mathbigdecimal.utils.toRadians
import java.math.BigDecimal
import java.math.MathContext

class RightTriangle(override val polygon: List<OffsetBD>) : Triangle(polygon) {

    companion object {
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
            val hypot = adjacent / cos(angleInRadians)
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
                acos(adjacent.divide(hypotenuse, MathContext(MATH_PRECISION_THREE))) ?: return null
            val angle = toDegrees(angleInRadians)
            return angle
        }
    }
}
