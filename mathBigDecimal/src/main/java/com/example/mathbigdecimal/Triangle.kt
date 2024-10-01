package com.example.mathbigdecimal

import java.math.BigDecimal

object Triangle {
    fun isValid(
        a: BigDecimal,
        b: BigDecimal,
        c: BigDecimal,
    ): Boolean {
        return a + b > c && a + c > b && c + b > a
    }
}