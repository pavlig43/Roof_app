package com.pavlig43.roofapp.ui.shapes

import androidx.annotation.DrawableRes
import com.pavlig43.roof_app.R

enum class Shapes(
    @DrawableRes val imageRes: Int,
    val readableName: String,
) {
    Triangle(R.drawable.triangle, "Треугольник"),
    Quadrilateral(R.drawable.quadro, "4-угольник"),
}
