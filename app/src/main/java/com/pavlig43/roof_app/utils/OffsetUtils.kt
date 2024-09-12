package com.pavlig43.roof_app.utils

import androidx.compose.ui.geometry.Offset
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH


/**
 * меняю координату Х , использую для координат листа железа, если через линейную интерполяцию нижняя или верхняя точка фигуры в пределах ширины этого листа
 */
fun Offset.replaceX(
    newPeakX: Float,
): Offset {
    return Offset(newPeakX, this.y)
}


fun Offset.changeCMToPx(
    oneMeterInHeightYtPx: Float,
    oneMeterInWidthXPx: Float,
    paddingWidth: Float = (A4WIDTH * 0.05).toFloat(),
    paddingHeight: Float = (A4HEIGHT * 0.05).toFloat()
): Offset {
    return Offset(
        (this.x.toDouble() / 100 * oneMeterInWidthXPx + paddingWidth).toFloat(),
        (this.y.toDouble() / 100 * oneMeterInHeightYtPx + paddingHeight).toFloat()
    )

}