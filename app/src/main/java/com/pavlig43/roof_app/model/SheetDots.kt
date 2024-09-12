package com.pavlig43.roof_app.model

import androidx.compose.ui.geometry.Offset
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH
import com.pavlig43.roof_app.utils.changeCMToPx


data class SheetDots(
    val leftBottom: Offset,
    val leftTop: Offset,
    val rightTop: Offset,
    val rightBottom: Offset
)
fun SheetDots.convertSheetDotToPx(
    oneMeterInHeightYtPx: Float,
    oneMeterInWidthXPx: Float,
    paddingWidth: Float = (A4WIDTH * 0.05).toFloat(),
    paddingHeight: Float = (A4HEIGHT * 0.05).toFloat(),


    ): SheetDots {

    fun Offset.transform(): Offset = this
        .changeCMToPx(
            oneMeterInHeightYtPx,
            oneMeterInWidthXPx,
            paddingWidth,
            paddingHeight
        )

    return this.copy(
        leftBottom = this.leftBottom.transform(),
        leftTop = this.leftTop.transform(),
        rightTop = this.rightTop.transform(),
        rightBottom = this.rightBottom.transform()
    )
}