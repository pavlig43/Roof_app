package com.pavlig43.roof_app.ui.shapes.triangle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.model.DotNameTriangle3Side
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.ui.calculation_tile_4scat.SaveNameFile
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.createFile
import com.pavlig43.roof_app.utils.pdfResult3SideTriangle
import com.pavlig43.roof_app.utils.pdfResult4Side
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFilePDF
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject


class TriangleViewModel  : ViewModel() {


    private val _geometryTriangle3SideShape: MutableStateFlow<GeometryTriangle3SideShape> =
        MutableStateFlow(GeometryTriangle3SideShape())

    val geometryTriangle3SideShape = _geometryTriangle3SideShape.asStateFlow()

    /**
     * Текущая точка в системе координат, у которой пользователь может менять координаты
     * у нижней левой точки всегда координаты (0,0) пользователь не может ее менять
     */

    private val _currentDotName =
        MutableStateFlow(_geometryTriangle3SideShape.value.leftBottom.name)

    /**
     * Текущая точка , которая выбранна пользователем
     */
    val currentDot: StateFlow<Dot> =
        combine(_geometryTriangle3SideShape, _currentDotName) { _, _ -> changeCurrentDot() }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                _geometryTriangle3SideShape.value.leftBottom
            )

    /**
     * Переменная, которая показывает может ли существовать треугольник

     */
    val isValid = _geometryTriangle3SideShape.map { newShape ->
        shapeIsValid(newShape)

    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)


    private fun shapeIsValid(geometryTriangle3SideShape: GeometryTriangle3SideShape): Boolean {
        val (leftBottom, top, rightBottom) = geometryTriangle3SideShape

        val determinant =
            (top.distanceX - leftBottom.distanceX) * (rightBottom.distanceY - leftBottom.distanceY) - (top.distanceY - leftBottom.distanceY) * (rightBottom.distanceX - leftBottom.distanceX)
        return determinant != 0f

    }



    fun changeCurrentDotName(dotNameTriangle3Side: DotNameTriangle3Side) {
        _currentDotName.value = dotNameTriangle3Side
    }

    private fun changeCurrentDot(): Dot {
        val dots = listOf(
            _geometryTriangle3SideShape.value.leftBottom,
            _geometryTriangle3SideShape.value.top,
            _geometryTriangle3SideShape.value.rightBottom
        )
        return dots.first { it.name == _currentDotName.value }
    }

    fun changeParamsDot(dot: Dot) {

        when (dot.name) {
            DotNameTriangle3Side.LEFTBOTTOM -> _geometryTriangle3SideShape.update {
                it.copy(
                    leftBottom = dot
                )
            }

            DotNameTriangle3Side.RIGHTBOTTOM -> _geometryTriangle3SideShape.update {
                it.copy(
                    rightBottom = dot
                )
            }

            DotNameTriangle3Side.TOP -> _geometryTriangle3SideShape.update { it.copy(top = dot) }
        }
    }



    /**
     * создает ПДФ файл треугольника

     */
    fun createPDFFile(context: Context, sheet: Sheet): File {
        val pdfDocument = PdfDocument()
        pdfDocument.pdfResult3SideTriangle(
            _geometryTriangle3SideShape.value,
            pageNumber = 1,
            sheet = sheet,
            single = true
        )
        val file = pdfDocument.createFile(context)
        return file
    }

}

/**
 * остроугольный треугольник
 */
data class GeometryTriangle3SideShape(
    val leftBottom: Dot = Dot(name = DotNameTriangle3Side.LEFTBOTTOM),
    val top: Dot = Dot(name = DotNameTriangle3Side.TOP, distanceX = 100f, distanceY = 100f,),
    val rightBottom: Dot = Dot(
        name = DotNameTriangle3Side.RIGHTBOTTOM,
        distanceX = 0f,
        distanceY = 200f,
        canChangeX = false
    )
)