package com.pavlig43.roofapp.ui.calculationTile4scat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.Triangle
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.calculateFromAngle
import com.pavlig43.roofapp.model.calculateFromHeight
import com.pavlig43.roofapp.model.calculateFromHypotenuse
import com.pavlig43.roofapp.model.updateMultiplicity
import com.pavlig43.roofapp.model.updateOverlap
import com.pavlig43.roofapp.model.updateWidthGeneral
import com.pavlig43.roofapp.utils.checkSaveName
import com.pavlig43.roofapp.utils.renderPDF
import com.pavlig43.roofapp.utils.saveFilePDF
import com.pavlig43.roofapp.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel @Inject constructor() : ViewModel() {
    private val _stateNavigation: MutableStateFlow<StateCalculationTile4Scat> =
        MutableStateFlow(StateCalculationTile4Scat.ChangeCalculation)
    val stateNavigation = _stateNavigation.asStateFlow()

    private val _roofState = MutableStateFlow(RoofParamsClassic4Scat())
    val roofState = _roofState.asStateFlow()

    /**
     * Список страниц ПДФ файла для отображения на экране
     */
    private val _listBitmap: MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()

    private val _pdfFile: MutableStateFlow<File?> =
        MutableStateFlow(null)

    private val _saveNameFile: MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    val isValid: StateFlow<Boolean> =
        _roofState.map {
            checkValid(it)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                checkValid(_roofState.value),
            )

    fun returnToCalculateScreen() {
        _stateNavigation.value = StateCalculationTile4Scat.ChangeCalculation
    }

    private fun checkValid(paramsState: RoofParamsClassic4Scat): Boolean {

        val a = paramsState.width.divide(BigDecimal(2))
        val b = paramsState.height
        val c = paramsState.hypotenuse
        Log.d("paramcheckValid", "$a-$b-$c")
       return Triangle.isValid(a, b, c) &&
                paramsState.sheet.widthGeneral > 0f &&
                paramsState.sheet.widthGeneral > paramsState.sheet.overlap

    }

    fun changeWidth(newWidth: BigDecimal) {
        val oldParams = _roofState.value

        _roofState.value = oldParams.copy(width = newWidth)
    }

    fun changeLen(newLen: BigDecimal) {
        val oldParams = _roofState.value
        _roofState.value = oldParams.copy(len = newLen)
    }

    fun updateFromHypotenuse(newHypotenuse: BigDecimal) {
        val oldParams = _roofState.value
        _roofState.value = oldParams.calculateFromHypotenuse(newHypotenuse)
    }

    fun updateFromHeight(newHeight: BigDecimal) {
        val oldParams = _roofState.value

        _roofState.value = oldParams.calculateFromHeight(newHeight)
    }

    fun updateFromAngle(newAngle: BigDecimal) {
        val oldParams = _roofState.value

        _roofState.value = oldParams.calculateFromAngle(newAngle)
    }

    fun changeWidthOfSheet(newWidth: Float) {
        _roofState.update { it.copy(sheet = it.sheet.updateWidthGeneral(newWidth)) }
    }

    fun changeOverlap(newOverlap: Float) {
        _roofState.update { it.copy(sheet = it.sheet.updateOverlap(newOverlap)) }
    }

    fun changeMultiplicity(newMultiplicity: Float) {
        _roofState.update { it.copy(sheet = it.sheet.updateMultiplicity(newMultiplicity)) }
    }

    fun getResult(context: Context) {
        val pdfDocument = PdfDocument()

        _pdfFile.value =
            pdfResultRoof4Scat(
                roofParamsClassic4Scat = _roofState.value,
                pdfDocument = pdfDocument,
                context = context,
            )
        _listBitmap.value = renderPDF(_pdfFile.value!!, viewModelScope)

        _stateNavigation.value = StateCalculationTile4Scat.GetDraw
    }

    fun shareFile(context: Context) {
        if (_pdfFile.value != null) {
            sharePDFFile(context, _pdfFile.value!!)
        }
    }

    fun saveFile(context: Context) {
        context.saveFilePDF(_pdfFile.value!!, saveNameFile = _saveNameFile.value.name)
        Log.d("save", _saveNameFile.value.name)
    }

    fun checkName(
        newName: String,
        context: Context,
    ) {
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