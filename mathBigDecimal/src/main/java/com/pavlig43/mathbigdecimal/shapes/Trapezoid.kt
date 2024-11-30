package com.pavlig43.mathbigdecimal.shapes

import com.pavlig43.mathbigdecimal.MATH_PRECISION
import com.pavlig43.mathbigdecimal.utils.sqrt1
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.logging.Logger

object Trapezoid {
    private val logger: Logger = Logger.getLogger(Trapezoid::class.java.name)

    fun smallFoot(
        bigFoot: BigDecimal,
        height: BigDecimal,
        edge: BigDecimal,
    ): BigDecimal =
        try {
            val smallCatheter = (edge * edge - height * height).sqrt1(MathContext(MATH_PRECISION))

            (bigFoot - smallCatheter.multiply(BigDecimal(2))).setScale(2, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            logger.severe(e.message)
            BigDecimal.ZERO
        }
}
