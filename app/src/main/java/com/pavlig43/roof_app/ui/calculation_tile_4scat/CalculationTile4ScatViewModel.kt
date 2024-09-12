package com.pavlig43.roof_app.ui.calculation_tile_4scat

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.model.RoofParamsClassic4ScatState
import com.pavlig43.roof_app.model.calculateFromAngle
import com.pavlig43.roof_app.model.calculateFromHeight
import com.pavlig43.roof_app.model.calculateFromHypotenuse
import com.pavlig43.roof_app.utils.calculateCmInM
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFilePDF
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CalculationTile4ScatViewModel @Inject constructor() : ViewModel() {

    private val _stateNavigation: MutableStateFlow<StateCalculationTile4Scat> =
        MutableStateFlow(StateCalculationTile4Scat.ChangeCalculation)
    val stateNavigation = _stateNavigation.asStateFlow()

    private val _paramsState = MutableStateFlow(RoofParamsClassic4ScatState())
    val paramsState = _paramsState.asStateFlow()

    private val _listBitmap:MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()

    private val _pdfFile:MutableStateFlow<File?> = MutableStateFlow(null)

    private val  _saveNameFile:MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    fun checkName(newName:String, context: Context,){
        val isValid = checkSaveName(newName, context)

        _saveNameFile.update { it.copy(name = newName, isValid = isValid)}

    }

    val isValid: StateFlow<Boolean> = _paramsState.map {
        checkValid(it)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly, false
        )
    fun returnToCalculateScreen(){
        _stateNavigation.value = StateCalculationTile4Scat.ChangeCalculation
    }


    private fun checkValid(paramsState: RoofParamsClassic4ScatState): Boolean {

        return try {
            val a = paramsState.width.toDouble() / 2
            val b = paramsState.height?.toDouble()
            val c = paramsState.hypotenuse?.toDouble()
            Log.d("paramcheckValid", "$a-$b-$c")
            a + b!! > c!! && a + c > b && b + c > a && paramsState.sheet.widthGeneral>0.0 && paramsState.sheet.widthGeneral>paramsState.sheet.overlap

        } catch (e: Exception) {
            false
        }


    }

    fun changeWidth(newWidth: String) {
        val oldParams = _paramsState.value
        _paramsState.value = oldParams.copy(width = newWidth)
    }

    fun changeLen(newLen: String) {

        val oldParams = _paramsState.value
        _paramsState.value = oldParams.copy(len = newLen)
    }

    fun updateFromHypotenuse(newHypotenuse: String) {
        val oldParams = _paramsState.value
        _paramsState.value = oldParams.calculateFromHypotenuse(newHypotenuse)
    }


    fun updateFromHeight(newHeight: String) {
        val oldParams = _paramsState.value

        _paramsState.value = oldParams.calculateFromHeight(newHeight)
    }

    fun updateFromAngle(newAngle: String) {
        val oldParams = _paramsState.value

        _paramsState.value = oldParams.calculateFromAngle(newAngle)
    }

    fun changeWidthOfSheet(newWidth: String){
    _paramsState.update { it.copy(sheet = it.sheet.copy(widthGeneral = calculateCmInM(newWidth))) }
}
    fun changeOverlap(newOverlap:String){
        _paramsState.update { it.copy(sheet = it.sheet.copy(overlap = calculateCmInM(newOverlap))) }
    }
    fun getResult(context: Context) {
        _pdfFile.value =
            roofResult4Scat(_paramsState.value,context)
        if (_pdfFile.value !=null){
            _listBitmap.value = renderPDF(_pdfFile.value!!,viewModelScope)
            _stateNavigation.value = StateCalculationTile4Scat.GetDraw
        }

    }

    fun shareFile(context: Context){
        if (_pdfFile.value !=null){
           sharePDFFile(context,_pdfFile.value!!)
        }

    }
    fun saveFile(context: Context){
        saveFilePDF(context, _pdfFile.value, saveNameFile = _saveNameFile.value.name)
        Log.d("save",_saveNameFile.value.name)


    }



}



sealed class StateCalculationTile4Scat {
    data object ChangeCalculation : StateCalculationTile4Scat()
    data object GetDraw : StateCalculationTile4Scat()
}

data class SaveNameFile(
    val name:String = "",
    val isValid:Boolean = false
)

