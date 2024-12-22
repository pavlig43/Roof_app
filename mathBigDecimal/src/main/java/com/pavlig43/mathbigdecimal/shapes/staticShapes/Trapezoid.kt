package com.pavlig43.mathbigdecimal.shapes.staticShapes

import com.pavlig43.mathbigdecimal.MATH_PRECISION_THREE
import com.pavlig43.mathbigdecimal.utils.sqrt1
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object Trapezoid {

    fun smallFoot(
        bigFoot: BigDecimal,
        height: BigDecimal,
        edge: BigDecimal,
    ): BigDecimal =
        try {
            val smallCatheter =
                (edge * edge - height * height).sqrt1(MathContext(MATH_PRECISION_THREE))

            (bigFoot - smallCatheter.multiply(BigDecimal(2))).setScale(2, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            println(e)
            BigDecimal.ZERO
        }
}
