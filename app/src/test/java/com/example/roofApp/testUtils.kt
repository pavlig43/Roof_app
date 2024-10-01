package com.example.roofApp

import org.junit.jupiter.api.Assertions
import java.math.BigDecimal

fun assertBigDecimalEquals(expected: BigDecimal, actual: BigDecimal, delta: BigDecimal = BigDecimal(0.03)) {
    // Вычисляем разницу
    val difference = expected.subtract(actual).abs()

    // Сравниваем разницу с дельтой
    if (difference > delta) {
        Assertions.fail<String>("Expected <$expected> but got <$actual> with delta <$delta>")
    }
}