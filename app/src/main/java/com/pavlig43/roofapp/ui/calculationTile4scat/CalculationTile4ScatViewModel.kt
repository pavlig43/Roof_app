package com.pavlig43.roofapp.ui.calculationTile4scat

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.shapes.Triangle
import com.pavlig43.roofapp.model.RoofParam
import com.pavlig43.roofapp.model.RoofParamName
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.updateRoofParams
import com.pavlig43.roofapp.model.updateSheetParams
import com.pavlig43.roofapp.utils.checkSaveName
import com.pavlig43.roofapp.utils.saveFilePDF
import com.pavlig43.roofapp.utils.sharePDFFile
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val _stateNavigation =
        MutableStateFlow<StateCalculationTile4Scat>(StateCalculationTile4Scat.ChangeCalculation)
    val stateNavigation = _stateNavigation.asStateFlow()

    private val _roofState = MutableStateFlow(RoofParamsClassic4Scat())
    val roofState = _roofState.asStateFlow()

    init {
        viewModelScope.launch { _roofState.collect { Log.d("testState", it.toString()) } }
    }

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
        Log.d("selectOPt", _selectedOptionDropMenu.value.toString())
    }

    private val pdfFile = MutableStateFlow<File?>(null)

    val pdfReaderState: StateFlow<VerticalPdfReaderState?> =
        pdfFile
            .filterNotNull()
            .map { file ->
                val uri = file.toUri()
                VerticalPdfReaderState(ResourceType.Local(uri))
            }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _saveNameFile = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    val isValid: StateFlow<Boolean> =
        _roofState
            .map {
                checkValid(it)
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                true,
            )

    fun returnToCalculateScreen() {
        _stateNavigation.value = StateCalculationTile4Scat.ChangeCalculation
    }

    private fun checkValid(paramsState: RoofParamsClassic4Scat): Boolean {
        val a = paramsState.width.value.divide(BigDecimal(2))
        val b = paramsState.height.value
        val c = paramsState.pokat.value
        Log.d("paramcheckValid", "$a-$b-$c")
        return Triangle.isValid(a, b, c) &&
                paramsState.sheet.widthGeneral.value > BigDecimal.ZERO &&
                paramsState.sheet.widthGeneral.value > paramsState.sheet.overlap.value &&
                paramsState.len.value >= paramsState.width.value
    }

    fun updateRoofParams(roofParam: RoofParam) {
        _roofState.update {
            it.updateRoofParams(roofParam)
        }
        changeSelectedOption(roofParam)
    }

    fun updateSheetParams(sheetParam: SheetParam) {
        _roofState.update { it.copy(sheet = it.sheet.updateSheetParams(sheetParam)) }
    }

    fun getResult() {
        val pdfDocument = PdfDocument()

        viewModelScope.launch {
            pdfFile.value =
                pdfResultRoof4Scat(
                    roofParamsClassic4Scat = _roofState.value,
                    pdfDocument = pdfDocument,
                    context = context,
                )
            _stateNavigation.value = StateCalculationTile4Scat.GetDraw
        }
    }

    fun shareFile() {
        if (pdfFile.value != null) {
            context.sharePDFFile(pdfFile.value!!)
        }
    }

    fun saveFile() {
        viewModelScope.launch {
            context.saveFilePDF(
                pdfFile.value!!,
                saveNameFile = _saveNameFile.value.name,
            )
        }

        Log.d("save", _saveNameFile.value.name)
    }

    fun checkName(newName: String) {
        val isValid = context.checkSaveName(newName)

        _saveNameFile.update { it.copy(name = newName, isValid = isValid) }
    }
}

sealed class StateCalculationTile4Scat {
    data object ChangeCalculation : StateCalculationTile4Scat()

    data object GetDraw : StateCalculationTile4Scat()
}

data class SaveNameFile(
    val name: String = "",
    val isValid: Boolean = false,
)
