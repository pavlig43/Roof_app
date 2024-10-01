package com.example.mathbigdecimal


import com.example.mathbigdecimal.utils.sqrt1
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object Trapezoid {
    fun smallFoot(
        bigFoot: BigDecimal,
        height: BigDecimal,
        edge: BigDecimal,
    ): BigDecimal {
        return try {
            val smallCatheter = (edge * edge - height*height).sqrt1(MathContext(10))

            (bigFoot - smallCatheter.multiply(BigDecimal(2))).setScale(2, RoundingMode.HALF_UP)



        } catch (e: ArithmeticException) {
            BigDecimal.ZERO
        }

    }
}
fun main() {
    val a = Trapezoid.smallFoot(
        bigFoot = BigDecimal(1100),
        height = BigDecimal(600),
        edge = BigDecimal(781)
    )

    println(a)
}