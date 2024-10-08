package com.example.roofApp

import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.calculateFromAngle
import com.pavlig43.roofapp.model.calculateFromHeight
import com.pavlig43.roofapp.model.calculateFromHypotenuse
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RoofParamsClassic4ScatTest {
    private val roofParams = RoofParamsClassic4Scat()

    @Test
    fun testCalculateFromAngle() {
        val result = roofParams.calculateFromAngle(BigDecimal("33.56"))
        assertBigDecimalEquals(BigDecimal("600"), result.hypotenuse)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle)
        assertBigDecimalEquals(BigDecimal("331.69"), result.height)
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova)
        assertBigDecimalEquals(BigDecimal("100.00"), result.smallFoot)
    }

    @Test
    fun testCalculateFromHeight() {
        val result = roofParams.calculateFromHeight(BigDecimal("331.69"))
        assertBigDecimalEquals(BigDecimal("600"), result.hypotenuse)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle, delta = BigDecimal(0.15))
        assertBigDecimalEquals(BigDecimal("331.69"), result.height)
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova)
        assertBigDecimalEquals(BigDecimal("100.00"), result.smallFoot)
    }

    @Test
    fun testCalculateFromHypotenuse() {
        val result = roofParams.calculateFromHypotenuse(BigDecimal("600"))
        assertBigDecimalEquals(BigDecimal("600"), result.hypotenuse)
        assertBigDecimalEquals(BigDecimal("33.56"), result.angle)
        assertBigDecimalEquals(BigDecimal("331.69"), result.height)
        assertBigDecimalEquals(BigDecimal("781.02"), result.yandova)
        assertBigDecimalEquals(BigDecimal("100.00"), result.smallFoot)
    }
}

fun javaVersion() {
    println(System.getProperty("java.version"))
    println(System.getProperty("java.runtime.version"))
    println(System.getProperty("java.home"))
    println(System.getProperty("java.vendor"))
    println(System.getProperty("java.vendor.url"))
    println(System.getProperty("java.class.path"))
}

fun main() {
    javaVersion()
}
