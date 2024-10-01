package com.pavlig43.roofapp.utils

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH

/**
 * меняю координату Х , использую для координат листа железа, если через линейную интерполяцию нижняя или верхняя точка фигуры в пределах ширины этого листа
 */
fun Offset.replaceX(newPeakX: Float): Offset {
    return Offset(newPeakX, this.y)
}

fun Offset.changeCMToPx(
    oneCMInHeightYtPx: Float,
    oneCMInWidthXPx: Float,
    paddingWidth: Float = (A4WIDTH * 0.05).toFloat(),
    paddingHeight: Float = (A4HEIGHT * 0.05).toFloat(),
): Offset {
    val offset =
        Offset(
            (this.x * oneCMInWidthXPx + paddingWidth),
            (this.y * oneCMInHeightYtPx + paddingHeight),
        )
    Log.d("OffsetThis", this.toString())
    Log.d("Offset", offset.toString())
    return offset // home
}
