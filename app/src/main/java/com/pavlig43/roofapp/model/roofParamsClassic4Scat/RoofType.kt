package com.pavlig43.roofapp.model.roofParamsClassic4Scat

import androidx.annotation.StringRes
import com.pavlig43.roof_app.R
import java.math.BigDecimal

enum class RoofType(@StringRes val title: Int, val angle: BigDecimal?) {
    None(R.string.its_values, null),
    Candle(R.string.candle, BigDecimal("40")),
    Standard(R.string.standard, BigDecimal("30")),
    Flat(R.string.flat, BigDecimal("25"))
}
