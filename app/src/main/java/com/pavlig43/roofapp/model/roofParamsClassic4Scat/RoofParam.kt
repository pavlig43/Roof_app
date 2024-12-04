package com.pavlig43.roofapp.model.roofParamsClassic4Scat

import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.UnitOfMeasurement
import java.math.BigDecimal

data class RoofParam(
    val name: RoofParamName,
    val value: BigDecimal = BigDecimal.ZERO,
    val unit: UnitOfMeasurement = UnitOfMeasurement.CM,
)

enum class RoofParamName(
    val title: Int,
) {
    WIDTH(R.string.width_roof),
    LEN(R.string.len_roof),
    ANGLE(R.string.angle_tilt),
    HEIGHT(R.string.height_roof),
    POKAT(R.string.pokat),
    SMALL_FOOT(R.string.small_foot),
    YANDOVA(R.string.yandova)
}
