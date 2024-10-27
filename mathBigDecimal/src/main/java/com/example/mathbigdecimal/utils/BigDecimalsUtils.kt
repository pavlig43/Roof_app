package com.example.mathbigdecimal.utils

import java.math.BigDecimal
import java.math.MathContext

fun BigDecimal.sqrt1(mathContext: MathContext): BigDecimal {
    val two = BigDecimal(2)
    var guess = BigDecimal(kotlin.math.sqrt(this.toDouble())) // Первоначальная оценка
    var previousGuess: BigDecimal

    do {
        previousGuess = guess
        guess =
            this
                .divide(guess, mathContext) // Делим число на текущую оценку
                .add(guess) // Суммируем с текущей оценкой
                .divide(two, mathContext) // Делим пополам
    } while (guess
            .subtract(previousGuess)
            .abs() > BigDecimal.ZERO
    ) // Пока разница не станет незначительной

    return guess
}

fun abs(value: BigDecimal): BigDecimal = kotlin.math.abs(value.toDouble()).toBigDecimal()
