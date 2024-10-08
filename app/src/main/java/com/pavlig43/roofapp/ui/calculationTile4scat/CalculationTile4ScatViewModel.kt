package com.pavlig43.roofapp.ui.calculationTile4scat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.Triangle
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.calculateFromAngle
import com.pavlig43.roofapp.model.calculateFromHeight
import com.pavlig43.roofapp.model.calculateFromHypotenuse
import com.pavlig43.roofapp.model.updateSheetParams

import com.pavlig43.roofapp.utils.checkSaveName
import com.pavlig43.roofapp.utils.renderPDF
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
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
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

    private val pathURI = _pdfFile.
    filterNotNull().
    map { file->
        Log.d("URI",file.toUri().toString())
        file.toUri() }.
    stateIn(viewModelScope,SharingStarted.Lazily,null)

    val pdfReaderState: StateFlow<VerticalPdfReaderState?> = pathURI.filterNotNull().map { uri->
        VerticalPdfReaderState(ResourceType.Local(uri))
    }.stateIn(viewModelScope,SharingStarted.Lazily,null)

    private val _saveNameFile: MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    val isValid: StateFlow<Boolean> =
        _roofState.map {
            checkValid(it)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                true,
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
                paramsState.sheet.widthGeneral.value > BigDecimal.ZERO &&
                paramsState.sheet.widthGeneral.value > paramsState.sheet.overlap.value &&
                paramsState.len > paramsState.width
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

    fun updateSheetParams(sheetParam: SheetParam) {
        _roofState.update { it.copy(sheet = it.sheet.updateSheetParams(sheetParam)) }
    }

    fun getResult() {
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

    fun shareFile() {
        if (_pdfFile.value != null) {
            context.sharePDFFile(_pdfFile.value!!)
        }
    }

    fun saveFile() {
        context.saveFilePDF(_pdfFile.value!!, saveNameFile = _saveNameFile.value.name)

        Log.d("save", _saveNameFile.value.name)
    }

    fun checkName(newName: String) {
        val isValid = context.checkSaveName(newName)

        Log.d("URIFILE",_pdfFile.value?.toUri().toString())
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
