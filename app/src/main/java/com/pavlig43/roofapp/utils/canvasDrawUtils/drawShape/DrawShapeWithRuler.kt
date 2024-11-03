package com.pavlig43.roofapp.utils.canvasDrawUtils.drawShape

import android.graphics.Canvas
import androidx.compose.ui.geometry.Offset
import com.pavlig43.roofapp.utils.canvasDrawUtils.CountPxInOneCM
import com.pavlig43.roofapp.utils.canvasDrawUtils.ShapeCanvas
import com.pavlig43.roofapp.utils.canvasDrawUtils.сoordinateSystem.coordinateSystem

fun Canvas.drawShapeWithRuler(
    shapeCanvas: ShapeCanvas,
    countPxInOneCM: CountPxInOneCM,
    startOffset: Offset = Offset(0F, 0F),
) {


    coordinateSystem(
        countCMInX = shapeCanvas.maxDistanceX.toInt(),
        countCMInY = shapeCanvas.maxDistanceY.toInt(),
        countPxInOneCM = countPxInOneCM,
        startOffsetLine = startOffset

    )


    drawShape(
        shapeCanvas = shapeCanvas,
        countPxInOneCM = countPxInOneCM,
        startOffset = startOffset,
    )
}
