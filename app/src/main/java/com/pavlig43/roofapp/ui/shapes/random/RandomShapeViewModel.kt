package com.pavlig43.roofapp.ui.shapes.random

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RandomShapeViewModel @Inject constructor() : ViewModel() {


//    val shapeBuild = MutableStateFlow(ShapeBuild)
}

data class ShapeBuildPoint(
    val numberOfDot: Int = 0,

    )

data class ShapeBuild(
    val listPoint: List<ShapeBuildPoint>
)

