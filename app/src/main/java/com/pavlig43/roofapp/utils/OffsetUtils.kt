package com.pavlig43.roofapp.utils

import androidx.compose.ui.geometry.Offset
import com.example.mathbigdecimal.OffsetBD

fun OffsetBD.toOffset() = Offset(x.toFloat(), y.toFloat())
