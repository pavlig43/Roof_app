package com.pavlig43.roofapp.ui.shapes.quadrilateral

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.example.pdfcanvasdraw.abstractCanvas.drawShape.ShapeCanvas
import com.example.pdfcanvasdraw.abstractCanvas.drawShape.drawShapeWithRulerAndFillRectangle
import com.example.pdfcanvasdraw.pdf.model.PageConfig
import com.example.pdfcanvasdraw.pdf.page.implementation.shape.ShapeWithRulerAndRectangleRenderer
import com.example.pdfcanvasdraw.pdf.page.implementation.shape.getCountPxInOneCM
import com.example.pdfcanvasdraw.pdf.renderDocument.PdfBuilder
import com.pavlig43.roofapp.model.Dot
import com.pavlig43.roofapp.model.DotName4Side
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.utils.PageContentBuilder
import com.pavlig43.roofapp.utils.createFile
import com.pavlig43.roofapp.utils.toPointF
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class QuadrilateralViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val _geometry4SideShape =
        MutableStateFlow(Geometry4SideShape())

    val geometry4SideShape = _geometry4SideShape.asStateFlow()

    /**
     * Текущая точка в системе координат, у которой пользователь может менять координаты
     * у нижней левой точки всегда координаты (0,0) пользователь не может ее менять
     */

    private val currentDotName = MutableStateFlow(_geometry4SideShape.value.leftBottom.name)

    /**
     * Текущая точка , которая выбранна пользователем
     */
    val currentDot: StateFlow<Dot> =
        combine(_geometry4SideShape, currentDotName) { _, _ ->
            changeCurrentDot()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, _geometry4SideShape.value.leftBottom)

    /**
     * Переменная, которая показывает является ли 4хугольник правильным выпуклым
     * не уверен , что на самом деле описывает все случаи
     * TODO() нужно посмотреть законы в геометрии
     */
    val isValid =
        _geometry4SideShape
            .map { newShape ->
                shapeIsValid(newShape)
            }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun shapeIsValid(geometry4SideShape: Geometry4SideShape): Boolean =
        geometry4SideShape.leftTop.PointF.x > BigDecimal.ZERO &&
                geometry4SideShape.rightTop.PointF.x > BigDecimal.ZERO &&
                geometry4SideShape.rightBottom.PointF.y != BigDecimal.ZERO

    fun changeCurrentDotName(dotName4Side: DotName4Side) {
        currentDotName.value = dotName4Side
    }

    private fun changeCurrentDot(): Dot {
        val dots =
            listOf(
                _geometry4SideShape.value.leftBottom,
                _geometry4SideShape.value.leftTop,
                _geometry4SideShape.value.rightTop,
                _geometry4SideShape.value.rightBottom,
            )
        return dots.first { it.name == currentDotName.value }
    }

    fun changeParamsDot(dot: Dot) {
        when (dot.name) {
            DotName4Side.LEFTTOP -> _geometry4SideShape.update { it.copy(leftTop = dot) }
            DotName4Side.LEFTBOTTOM -> _geometry4SideShape.update { it.copy(leftBottom = dot) }
            DotName4Side.RIGHTBOTTOM ->
                _geometry4SideShape.update {
                    it.copy(
                        rightBottom = dot,
                    )
                }

            DotName4Side.RIGHTTOP -> _geometry4SideShape.update { it.copy(rightTop = dot) }
        }
    }

    /**
     * создает ПДФ файл 4-х угольника

     */
    suspend fun createPDFFile(sheet: Sheet): File {

        val a = CoordinateShape(
            _geometry4SideShape.value.tolst(),
            isMoveToPositiveQuadrant = true
        )
        val sc = a.toShapeCanvas()
        val listOfRectangle =
            a.fillShapeWithRectangles(sheet.widthGeneral.value, sheet.overlap.value)
                .map { rightRectangle ->
                    rightRectangle.polygon.map { it.toPointF() }.run {
                        ShapeCanvas(
                            this
                        )
                    }
                }
        val pageConfig = PageConfig()
        val countPxInOneCM = pageConfig.getCountPxInOneCM(sc)
        val arr = listOf(
            PageContentBuilder(
                pageConfig = pageConfig,
                generateDraw = {
                    drawShapeWithRulerAndFillRectangle(
                        shapeCanvas = sc,

                        countPxInOneCM = countPxInOneCM,
                        listOfRectangle = listOfRectangle,
                        startPointF = pageConfig.startPointF

                    )
                }
            ),
        )

        val pdArr = ShapeWithRulerAndRectangleRenderer(
            shapeCanvas = sc,
            listOfRectangle = listOfRectangle
        )
        val pdfDocumentNew: PdfDocument = PdfBuilder().createPdf(
            listOf(pdArr)
        )

//        pdfDocument.renderContent(arr)
        val file = pdfDocumentNew.createFile(context)
        return file
    }
}

data class Geometry4SideShape(
    val leftBottom: Dot = Dot(name = DotName4Side.LEFTBOTTOM),
    val leftTop: Dot =
        Dot(
            name = DotName4Side.LEFTTOP,
            canMinusY = true,
            PointF =
            OffsetBD(
                x = BigDecimal("500"),
                y = BigDecimal("-200"),
            ),
        ),
    val rightTop: Dot =
        Dot(
            name = DotName4Side.RIGHTTOP,
            canMinusY = true,
            PointF =
            OffsetBD(
                x = BigDecimal("400"),
                y = BigDecimal("200"),
            ),
        ),
    val rightBottom: Dot =
        Dot(
            name = DotName4Side.RIGHTBOTTOM,
            canMinusX = true,
            PointF =
            OffsetBD(
                x = BigDecimal("-200"),
                y = BigDecimal("400"),
            ),
        ),
)

fun Geometry4SideShape.tolst(): List<OffsetBD> {
    return listOf(leftBottom.PointF, leftTop.PointF, rightTop.PointF, rightBottom.PointF)
}

fun CoordinateShape.toShapeCanvas(): ShapeCanvas {
    return ShapeCanvas(this.polygon.map { it.toPointF() })
}
