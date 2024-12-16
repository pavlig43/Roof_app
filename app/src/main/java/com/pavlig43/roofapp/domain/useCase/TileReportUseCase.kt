package com.pavlig43.roofapp.domain.useCase

import com.pavlig43.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.drawText.drawTextOnSeveralPages
import com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.shape.ShapeWithRulerAndRectangleRenderer
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.DocBuilder
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.MARK_INFO
import com.pavlig43.roofapp.data.resourceProvider.ResourceProvider
import com.pavlig43.roofapp.mappers.coordinateShape.toShapeCanvas
import com.pavlig43.roofapp.model.Sheet

/**
 * Класс TileReportUseCase предназначен для создания отчёта в виде документа, используя объект
 * [DocBuilder]. Метод [invoke] принимает список объектов [CoordinateShape],
 * объект [Sheet] с параметрами листа и необязательный список текстовой информации [otherInfo].
 * Сначала каждая форма из [listOfCoordinateShape] ,состоящая из листов [Sheet]
 * заполняется прямоугольниками с помощью метода  [fillShapeWithRectangles],
 * учитывающего ширину и перехлест листа из [sheet]. Затем для каждого
 * прямоугольника создаётся новый объект [Sheet], где длина листа соответствует высоте прямоугольника.
 * Используя пары исходных форм и списков прямоугольников, создаются страницы отчёта с помощью
 * класса [ShapeWithRulerAndRectangleRenderer], который отображает начальную фигуру [CoordinateShape]
 * и прямоугольники на холсте.
 * На основе данных о листах формируется текстовая информация: подсчитывается количество листов
 * каждой длины, а результат дополняется единицами измерения и передаётся в список [infoText],
 * куда также добавляется [otherInfo]. Текстовая информация рендерится через объект [DrawText].
 * Все страницы, включая рендеринг форм и текст, передаются в метод [createAndGetFileName] из
 * [DocBuilder] для генерации и сохранения файла.
 */
class TileReportUseCase(
    private val docBuilder: DocBuilder,
    private val resourceProvider: ResourceProvider
) {
    suspend operator fun invoke(
        listOfCoordinateShape: List<CoordinateShape>,
        sheet: Sheet,
        otherInfo: List<String> = listOf()
    ): String {
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
            listOfRealSheet.flatten().sortedByDescending { it.ceilLen }
                .groupingBy { it.ceilLen }.eachCount()
                .map { (len, count) ->
                    "$len (${resourceProvider.getString(sheet.overlap.unit.title)}) - $count"
                }.toMutableList().apply { add(0, resourceProvider.getString(R.string.sheets)) }

        val infoText = infoSheetText + otherInfo

        val drawTextRenderer = infoText.drawTextOnSeveralPages(markInfo = MARK_INFO)

        val allPages = renderShapePages + drawTextRenderer

        return docBuilder.createAndGetFilePath(allPages)
    }
}
