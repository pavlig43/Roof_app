package com.pavlig43.mathbigdecimal.shapes.staticShapes

import com.pavlig43.mathbigdecimal.OffsetBD
import java.math.BigDecimal

open class Triangle(override val polygon: List<OffsetBD>) : StaticIsConvexShape() {
    companion object {
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
}
