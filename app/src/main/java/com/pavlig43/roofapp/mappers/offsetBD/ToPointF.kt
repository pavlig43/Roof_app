package com.pavlig43.roofapp.mappers.offsetBD

import android.graphics.PointF
import com.example.mathbigdecimal.OffsetBD

fun OffsetBD.toPointF() = PointF(x.toFloat(), y.toFloat())
