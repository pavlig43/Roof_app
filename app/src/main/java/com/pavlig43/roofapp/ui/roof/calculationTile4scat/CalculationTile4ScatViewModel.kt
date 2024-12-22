package com.pavlig43.roofapp.ui.roof.calculationTile4scat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.mathbigdecimal.OffsetBD
import com.pavlig43.mathbigdecimal.shapes.shapeForDraw.CoordinateShape
import com.pavlig43.mathbigdecimal.shapes.staticShapes.Triangle
import com.pavlig43.roofapp.data.resourceProvider.AndroidResourceProvider
import com.pavlig43.roofapp.di.DocType
import com.pavlig43.roofapp.di.DocTypeBuilder
import com.pavlig43.roofapp.domain.useCase.TileReportUseCase
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParam
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamName
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofType
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.calculateFromRoofType
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.updateRoofParams
import com.pavlig43.roofapp.model.updateSheetParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel
@Inject constructor(
    @DocTypeBuilder(DocType.AndroidPdf) private val tileReportUseCase: TileReportUseCase,
    private val resourceProvider: AndroidResourceProvider
) : ViewModel() {

    private val _roofState = MutableStateFlow(RoofParamsClassic4Scat())
    val roofState = _roofState.asStateFlow()

    private val _sheet = MutableStateFlow(Sheet())
    val sheet = _sheet.asStateFlow()

    private val _selectedOptionDropMenu = MutableStateFlow(_roofState.value.pokatTrapezoid)

    val selectedOptionDropMenu = _selectedOptionDropMenu.asStateFlow()

    fun changeSelectedOption(roofParam: RoofParam) {
        _selectedOptionDropMenu.update {
            when (roofParam.name) {
                RoofParamName.ANGLE -> _roofState.value.angle
                RoofParamName.HEIGHT -> _roofState.value.height
                RoofParamName.POKAT_TRAPEZOID -> _roofState.value.pokatTrapezoid
                else -> _selectedOptionDropMenu.value
            }
        }
    }

    val isValid: StateFlow<Boolean> = _roofState.map {
        checkValid(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false,
    )

    private fun checkValid(paramsState: RoofParamsClassic4Scat): Boolean {
        val a = paramsState.width.value.divide(BigDecimal(2))
        val b = paramsState.height.value
        val c = paramsState.pokatTrapezoid.value
        Log.d("paramcheckValid", "$a-$b-$c")
        return Triangle.isValid(a, b, c) && paramsState.len.value >= paramsState.width.value
    }

    fun calculateFromRoofType(roofType: RoofType) {
        _roofState.update {
            it.calculateFromRoofType(roofType)
        }
        changeSelectedOption(_selectedOptionDropMenu.value)
    }

    fun updateRoofParams(roofParam: RoofParam) {
        _roofState.update {
            it.updateRoofParams(roofParam)
        }
        changeSelectedOption(roofParam)
    }

    fun updateSheetParams(sheetParam: SheetParam) {
        _sheet.update { it.updateSheetParams(sheetParam) }
    }

    private fun otherInfo(): List<String> {
        val listOfRoofParam = with(_roofState.value) {
            listOf(
                width,
                len,
                angle,
                height,
                pokatTrapezoid,
                pokatTriangle,
                yandova,
                calculateStandardRidge
            ).map {
                "${resourceProvider.getString(it.name.title)} - ${
                    it.value.setScale(
                        2,
                        RoundingMode.HALF_UP
                    )
                } (${
                    resourceProvider.getString(
                        it.unit.title
                    )
                })"
            }
        }
        return listOfRoofParam
    }

    fun getResult(moveToPdfResult: (String) -> Unit) {
        val lstCoordinateShape = _roofState.value.run {
            listOf(
                this.toTrapezoidCoordinateShape(),
                this.toTriangleCoordinateShape()
            )
        }
        viewModelScope.launch {
            val fileName = tileReportUseCase.invoke(
                listOfCoordinateShape = lstCoordinateShape,
                sheet = _sheet.value,
                otherInfo = otherInfo()
            )
            moveToPdfResult(fileName)
        }
    }
}

private fun RoofParamsClassic4Scat.toTrapezoidCoordinateShape() = CoordinateShape(
    listOf(
        OffsetBD.Zero,
        OffsetBD(
            pokatTrapezoid.value,
            (len.value - calculateStandardRidge.value).div(BigDecimal(2))
        ),
        OffsetBD(
            pokatTrapezoid.value,
            (len.value + calculateStandardRidge.value).div(BigDecimal(2))
        ),
        OffsetBD(BigDecimal.ZERO, len.value)
    )
)

private fun RoofParamsClassic4Scat.toTriangleCoordinateShape() = CoordinateShape(
    listOf(
        OffsetBD.Zero,
        OffsetBD(pokatTriangle.value, width.value.div(BigDecimal("2"))),
        OffsetBD(BigDecimal.ZERO, width.value)
    )
)
