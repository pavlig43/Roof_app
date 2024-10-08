package com.example.mathbigdecimal.utils

import java.math.BigDecimal
import java.math.RoundingMode


fun hypot(
    catheter1:BigDecimal,
    catheter2:BigDecimal,

): BigDecimal {
    val a = kotlin.math.hypot(catheter1.toDouble(),catheter2.toDouble())
    return BigDecimal(a).setScale(2,RoundingMode.HALF_UP)
}

fun acos(value: BigDecimal): BigDecimal? {
    val doubleValue = value.toDouble()
    return if (doubleValue in -1.0..1.0 ) kotlin.math.acos(doubleValue).toBigDecimal() else null

}
fun cos(value: BigDecimal): BigDecimal {

    return kotlin.math.cos(value.toDouble()).toBigDecimal()
}


fun atan(value:BigDecimal): BigDecimal {
    return kotlin.math.atan(value.toDouble()).toBigDecimal()
}
fun toDegrees(valueInRadian:BigDecimal): BigDecimal {
    return Math.toDegrees(valueInRadian.toDouble()).toBigDecimal()
}
fun toRadians(angle:BigDecimal): BigDecimal {
    return Math.toRadians(angle.toDouble()).toBigDecimal()
}
fun tan(value: BigDecimal): BigDecimal {
    return kotlin.math.tan(value.toDouble()).toBigDecimal()
}



fun main() {
    val b = acos((0.6).toBigDecimal())
    println(b)

}