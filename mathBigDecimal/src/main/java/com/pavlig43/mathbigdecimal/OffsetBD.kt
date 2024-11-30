package com.pavlig43.mathbigdecimal

import com.pavlig43.mathbigdecimal.utils.hypot
import java.math.BigDecimal
import java.math.RoundingMode

@Suppress("TooManyFunctions")
class OffsetBD(
    val x: BigDecimal,
    val y: BigDecimal,
) {
    operator fun component1(): BigDecimal = x

    operator fun component2(): BigDecimal = y

    companion object {
        val Zero = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO)
    }

    val absoluteValue: OffsetBD
        get() = OffsetBD(x.abs(), y.abs())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OffsetBD) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    /**
     * Returns a copy of this Offset instance optionally overriding the
     * x or y parameter
     */
    fun copy(
        x: BigDecimal = this.x,
        y: BigDecimal = this.y,
    ) = OffsetBD(x, y)

    /**
     * The magnitude of the offset.
     *

     */

    fun getSide(): BigDecimal = hypot(x, y)

    fun getDistance(otherDot: OffsetBD): BigDecimal {
        val dx = otherDot.x - this.x
        val dy = otherDot.y - this.y
        return hypot(dx, dy)
    }

    operator fun minus(other: OffsetBD): OffsetBD =
        OffsetBD(
            x - other.x,
            y - other.y,
        )

    operator fun plus(other: OffsetBD): OffsetBD =
        OffsetBD(
            x + other.x,
            y + other.y,
        )

    operator fun times(operand: BigDecimal): OffsetBD =
        OffsetBD(
            x.multiply(operand),
            y.multiply(operand),
        )

    fun divide(
        operand: BigDecimal,
        scale: Int = 10,
    ): OffsetBD =
        if (operand == BigDecimal.ZERO) {
            throw ArithmeticException("Division by zero")
        } else {
            OffsetBD(
                x.divide(operand, scale, RoundingMode.HALF_UP),
                y.divide(operand, scale, RoundingMode.HALF_UP),
            )
        }

    override fun toString() = "Offset($x, $y)"

    fun compareTo(other: OffsetBD): Int {
        val xComparison = this.x.compareTo(other.x)
        val yComparison = this.y.compareTo(other.y)
        return if (xComparison != 0 || yComparison != 0) 1 else 0
    }
}
