package com.pavlig43.roofapp.mappers.coordinateShape

import com.pavlig43.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.extentions.drawShape.ShapeOnCanvas
import com.pavlig43.roofapp.mappers.offsetBD.toPointF

fun CoordinateShape.toShapeCanvas(nameValue: String = ""): ShapeOnCanvas {
    return ShapeOnCanvas(this.polygon.map { it.toPointF() }, nameValue)
}
