package com.pavlig43.roofapp.ui.shapes.triangle

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.Triangle
import com.pavlig43.roofapp.model.Dot
import com.pavlig43.roofapp.model.DotNameTriangle3Side
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.utils.createFile
import com.pavlig43.roofapp.utils.pdfResult3SideTriangle
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
class TriangleViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val _geometryTriangle3SideShape =
        MutableStateFlow(GeometryTriangle3SideShape())

    val geometryTriangle3SideShape = _geometryTriangle3SideShape.asStateFlow()

    /**
     * Текущая точка в системе координат, у которой пользователь может менять координаты
     * у нижней левой точки всегда координаты (0,0) пользователь не может ее менять
     */

    private val currentDotName =
        MutableStateFlow(_geometryTriangle3SideShape.value.leftBottom.name)

    /**
     * Текущая точка , которая выбранна пользователем
     */
    val currentDot: StateFlow<Dot> =
        combine(_geometryTriangle3SideShape, currentDotName) { _, _ ->
            changeCurrentDot()
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _geometryTriangle3SideShape.value.leftBottom,
        )

    /**
     * Переменная, которая показывает может ли существовать треугольник

     */
    val isValid =
        _geometryTriangle3SideShape
            .map { newShape ->
                shapeIsValid(newShape)
            }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun shapeIsValid(geometryTriangle3SideShape: GeometryTriangle3SideShape): Boolean {
        val (leftBottom, top, rightBottom) = geometryTriangle3SideShape

        return Triangle.isValid(leftBottom.PointF, rightBottom.PointF, top.PointF)
    }

    fun changeCurrentDotName(dotNameTriangle3Side: DotNameTriangle3Side) {
        currentDotName.value = dotNameTriangle3Side
    }

    private fun changeCurrentDot(): Dot {
        val dots =
            listOf(
                _geometryTriangle3SideShape.value.leftBottom,
                _geometryTriangle3SideShape.value.top,
                _geometryTriangle3SideShape.value.rightBottom,
            )
        return dots.first { it.name == currentDotName.value }
    }

    fun changeParamsDot(dot: Dot) {
        when (dot.name) {
            DotNameTriangle3Side.LEFTBOTTOM ->
                _geometryTriangle3SideShape.update {
                    it.copy(
                        leftBottom = dot,
                    )
                }

            DotNameTriangle3Side.RIGHTBOTTOM ->
                _geometryTriangle3SideShape.update {
                    it.copy(
                        rightBottom = dot,
                    )
                }

            DotNameTriangle3Side.TOP ->
                _geometryTriangle3SideShape.update {
                    it.copy(
                        top = dot,
                    )
                }
        }
    }

    /**
     * создает ПДФ файл треугольника

     */
    suspend fun createPDFFile(sheet: Sheet): File {
        val pdfDocument = PdfDocument()
        pdfDocument.pdfResult3SideTriangle(
            context = context,
            _geometryTriangle3SideShape.value,
            pageNumber = 1,
            sheet = sheet,
            single = true,
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
    val top: Dot =
        Dot(
            name = DotNameTriangle3Side.TOP,
            PointF = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
        ),
    val rightBottom: Dot =
        Dot(
            name = DotNameTriangle3Side.RIGHTBOTTOM,
            PointF = OffsetBD(BigDecimal.ZERO, BigDecimal.ZERO),
            canChangeX = false,
        ),
)
