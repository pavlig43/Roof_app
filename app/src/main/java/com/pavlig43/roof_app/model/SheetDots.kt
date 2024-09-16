package com.pavlig43.roof_app.model

import androidx.compose.ui.geometry.Offset
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH
import com.pavlig43.roof_app.utils.changeCMToPx
import com.pavlig43.roof_app.utils.replaceX


data class SheetDots(
    val leftBottom: Offset,
    val leftTop: Offset,
    val rightTop: Offset,
    val rightBottom: Offset
)

/**
 * все координаты листа в см переводятся через кокоэффициенты масштабы в пиксели
 */
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

/**
 * Заменяет "Х" в координатах листа(минимальный в нижних точках листа, максимальный в верхних точках листа)
 * [intervalYForXMax] и [intervalYForXMax] это интервалы по "У" при которых "Х" имеет пиковое значение
 * Эти интервалы могут иметь как и последовательность из нескольких значений, если углы при основании равны 90°(часть фигуры стремится к прямоуголбнику),
 * так и интервал типа 10..10 в случаях , если фигура развернута в плоскости таким образом, что ее пиковые значения по "Х" представляют собой точки
 * если интервал крайних точек листа попадает в интервал одного из пиков, то в координатах листа заменяем "Х"
 */
fun SheetDots.replaceX(
    peakXMax: Float,
    peakXMin: Float,
    intervalYForXMax: ClosedFloatingPointRange<Float>,
    intervalYForXMin: ClosedFloatingPointRange<Float>,

    ): SheetDots {
    val sheetIntervalY = this.leftTop.y..this.rightTop.y
    var result = this
    if (sheetIntervalY.start <= intervalYForXMax.endInclusive && intervalYForXMax.start <= sheetIntervalY.endInclusive) {
        result = result.copy(
            leftTop = this.leftTop.replaceX(peakXMax),
            rightTop = this.rightTop.replaceX(peakXMax)
        )
    }
    if (sheetIntervalY.start <= intervalYForXMin.endInclusive && intervalYForXMin.start <= sheetIntervalY.endInclusive) {
        result = result.copy(
            leftBottom = this.leftBottom.replaceX(peakXMin),
            rightBottom = this.rightBottom.replaceX(peakXMin)
        )
    }
    return result
}