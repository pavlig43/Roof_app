package com.pavlig43.roofapp.domain

import com.pavlig43.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.drawText.DrawText
import com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.shape.ShapeWithRulerAndRectangleRenderer
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.DocBuilder
import com.pavlig43.roofapp.SHEETS
import com.pavlig43.roofapp.mappers.coordinateShape.toShapeCanvas
import com.pavlig43.roofapp.model.Sheet
import javax.inject.Inject

class TileReportUseCase @Inject constructor(
    private val docBuilder: DocBuilder,
) {
    suspend operator fun invoke(
        listOfCoordinateShape: List<CoordinateShape>,
        sheet: Sheet,
        otherInfo: List<String> = listOf()
    ) {
        val listOfRightRectangle = listOfCoordinateShape.map {
            it.fillShapeWithRectangles(
                sheet.widthGeneral.value,
                sheet.overlap.value
            )
        }
        val listOfRealSheet =
            listOfRightRectangle.map { it.map { rightRectangle -> sheet.copy(len = rightRectangle.height) } }
        val renderShapePages = listOfCoordinateShape.zip(listOfRightRectangle)
            .mapIndexed { pageIndex, (coordinateShape, listOfRightRectangle) ->
                ShapeWithRulerAndRectangleRenderer(
                    shapeOnCanvas = coordinateShape.toShapeCanvas(),
                    listOfRectangle = listOfRightRectangle.mapIndexed { index, rightRectangle ->
                        rightRectangle.toShapeCanvas(listOfRealSheet[pageIndex][index].ceilLen.toPlainString())
                    }
                )
            }
        val infoSheetText =
            listOfRealSheet.flatten().groupingBy { it.ceilLen }.eachCount().map { (len, count) ->
                "$len (${sheet.overlap.unit}) - $count"
            }.toMutableList().apply { add(0, SHEETS) }

        val infoText = infoSheetText + otherInfo

        val drawTextRenderer = DrawText(infoText)

        val allPages = renderShapePages + drawTextRenderer
        docBuilder.createAndGetFileName(allPages)
    }
}
