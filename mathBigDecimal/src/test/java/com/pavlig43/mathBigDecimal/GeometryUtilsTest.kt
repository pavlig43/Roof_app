package com.pavlig43.mathBigDecimal

import com.pavlig43.mathbigdecimal.utils.acos
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class GeometryUtilsTest {
    @Test
    fun `calculate acos`() {
        val acosResult = acos(BigDecimal("8.3"))
        assertEquals(null, acosResult)
    }
}
