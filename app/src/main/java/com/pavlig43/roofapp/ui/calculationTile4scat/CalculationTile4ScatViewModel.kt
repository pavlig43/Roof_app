package com.pavlig43.roofapp.ui.calculationTile4scat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.example.mathbigdecimal.shapes.Triangle
import com.pavlig43.roofapp.di.DocType
import com.pavlig43.roofapp.di.DocTypeBuilder
import com.pavlig43.roofapp.domain.TileReportUseCase
import com.pavlig43.roofapp.model.RoofParam
import com.pavlig43.roofapp.model.RoofParamName
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.updateRoofParams
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
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel
@Inject constructor(
    @DocTypeBuilder(DocType.AndroidPdf) private val tileReportUseCase: TileReportUseCase,
) : ViewModel() {

    private val _roofState = MutableStateFlow(RoofParamsClassic4Scat())
    val roofState = _roofState.asStateFlow()

    private val _sheet = MutableStateFlow(Sheet())
    val sheet = _sheet.asStateFlow()

    private val _selectedOptionDropMenu = MutableStateFlow(_roofState.value.pokat)

    val selectedOptionDropMenu = _selectedOptionDropMenu.asStateFlow()

    fun changeSelectedOption(roofParam: RoofParam) {
        _selectedOptionDropMenu.update {
            when (roofParam.name) {
                RoofParamName.ANGLE -> _roofState.value.angle
                RoofParamName.HEIGHT -> _roofState.value.height
                RoofParamName.POKAT -> _roofState.value.pokat
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
        val c = paramsState.pokat.value
        Log.d("paramcheckValid", "$a-$b-$c")
        return Triangle.isValid(a, b, c) && paramsState.len.value >= paramsState.width.value
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

    fun getResult() {
        val lstCoordinateShape = _roofState.value.run {
            listOf(
                this.toTrapezoidCoordinateShape(),
                this.toTriangleCoordinateShape()
            )
        }
        viewModelScope.launch {
            tileReportUseCase.invoke(
                listOfCoordinateShape = lstCoordinateShape,
                sheet = _sheet.value,
            )
        }
    }
}

private fun RoofParamsClassic4Scat.toTrapezoidCoordinateShape() = CoordinateShape(
    listOf(
        OffsetBD.Zero,
        OffsetBD(pokat.value, (len.value - smallFoot).div(BigDecimal(2))),
        OffsetBD(pokat.value, (len.value + smallFoot).div(BigDecimal(2))),
        OffsetBD(BigDecimal.ZERO, len.value)
    )
)

private fun RoofParamsClassic4Scat.toTriangleCoordinateShape() = CoordinateShape(
    listOf(
        OffsetBD.Zero,
        OffsetBD(pokat.value, width.value.div(BigDecimal("2"))),
        OffsetBD(BigDecimal.ZERO, width.value)
    )
)
