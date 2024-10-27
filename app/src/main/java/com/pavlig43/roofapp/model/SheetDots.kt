package com.pavlig43.roofapp.model

import com.example.mathbigdecimal.OffsetBD
import com.pavlig43.roofapp.A4HEIGHT
import com.pavlig43.roofapp.A4WIDTH
import com.pavlig43.roofapp.PADDING_PERCENT
import java.math.BigDecimal

data class SheetDots(
    val leftBottom: OffsetBD,
    val leftTop: OffsetBD,
    val rightTop: OffsetBD,
    val rightBottom: OffsetBD,
)

/**
 * все координаты листа в см переводятся через кокоэффициенты масштабы в пиксели
 */
fun SheetDots.convertSheetDotToPx(
    oneMeterInHeightYtPx: BigDecimal,
    oneMeterInWidthXPx: BigDecimal,
    paddingWidth: BigDecimal = (A4WIDTH * PADDING_PERCENT).toBigDecimal(),
    paddingHeight: BigDecimal = (A4HEIGHT * PADDING_PERCENT).toBigDecimal(),
): SheetDots {
    fun OffsetBD.transform(): OffsetBD =

        this.changeOffset(
            oneUnitInHeightYtPx = oneMeterInHeightYtPx,
            oneUnitInWidthXPx = oneMeterInWidthXPx,
            paddingXOffset = paddingWidth,
            paddingYOffset = paddingHeight,
        )

    return this.copy(
        leftBottom = this.leftBottom.transform(),
        leftTop = this.leftTop.transform(),
        rightTop = this.rightTop.transform(),
        rightBottom = this.rightBottom.transform(),
    )
}

/**
 * Заменяет "Х" в координатах листа(минимальный в нижних точках листа, максимальный в верхних точках листа)
 * [intervalYForXMax] и [intervalYForXMax] это интервалы по "У" при которых "Х" имеет пиковое значение
 * Эти интервалы могут иметь как и последовательность из нескольких значений,
 * если углы при основании равны 90°(часть фигуры стремится к прямоуголбнику),
 * так и интервал типа 10..10 в случаях , если фигура развернута в плоскости таким образом,
 * что ее пиковые значения по "Х" представляют собой точки
 * если интервал крайних точек листа попадает в интервал одного из пиков,
 * то в координатах листа заменяем "Х"
 */
fun SheetDots.replaceX(
    peakXMax: BigDecimal,
    peakXMin: BigDecimal,
    intervalYForXMax: ClosedRange<BigDecimal>,
    intervalYForXMin: ClosedRange<BigDecimal>,
): SheetDots {
    val sheetIntervalY = this.leftTop.y..this.rightTop.y
    var result = this
    if (sheetIntervalY.start <= intervalYForXMax.endInclusive &&
        intervalYForXMax.start <= sheetIntervalY.endInclusive
    ) {
        result =
            result.copy(
                leftTop = this.leftTop.copy(x = peakXMax),
                rightTop = this.rightTop.copy(x = peakXMax),
            )
    }
    if (sheetIntervalY.start <= intervalYForXMin.endInclusive &&
        intervalYForXMin.start <= sheetIntervalY.endInclusive
    ) {
        result =
            result.copy(
                leftBottom = this.leftBottom.copy(peakXMin),
                rightBottom = this.rightBottom.copy(peakXMin),
            )
    }
//    Log.d("")
    return result
}
