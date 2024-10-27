package com.example.mathbigdecimal.shapes

import com.example.mathbigdecimal.OffsetBD

class RightRectangle(listOfBasicsDots: List<OffsetBD>) : CoordinateShape(listOfBasicsDots) {
    private val leftBottom = OffsetBD(peakXMin, peakYMin)
    private val leftTop = OffsetBD(peakXMax, peakYMin)
    private val rightTop = OffsetBD(peakXMax, peakYMax)
    private val rightBottom = OffsetBD(peakXMin, peakYMax)

    /**
     * Заменяет "Х" в координатах (минимальный в нижних точках прямоугольника,
     * максимальный в верхних точках ) для того, чтобы растянуть этот [RightRectangle]
     * по всей высоте фигуры, которую нужно закрасить [RightRectangle]
     */
    fun replaceX(
        parentShape: CoordinateShape,
    ): RightRectangle {
        val intervalY = peakYMin..peakYMax
        val result: MutableList<OffsetBD> = mutableListOf()

        val topIntersect = intervalY.start <= parentShape.peakYRangeMax.endInclusive &&
                parentShape.peakYRangeMax.start <= intervalY.endInclusive


        val bottomIntersect = intervalY.start <= parentShape.peakYRangeMin.endInclusive &&
                parentShape.peakYRangeMin.start <= intervalY.endInclusive

        result.add(
            if (bottomIntersect) leftBottom.copy(x = peakXMin) else leftBottom
        )

        result.add(
            if (topIntersect) leftTop.copy(x = peakXMax) else leftTop
        )

        result.add(
            if (topIntersect) rightTop.copy(x = peakXMax) else rightTop
        )

        result.add(
            if (bottomIntersect) rightBottom.copy(x = peakXMin) else rightBottom
        )

        return RightRectangle(result)
    }
}
