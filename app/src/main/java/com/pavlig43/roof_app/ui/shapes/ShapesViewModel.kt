package com.pavlig43.roof_app.ui.shapes

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.updateMultiplicity
import com.pavlig43.roof_app.model.updateOverlap
import com.pavlig43.roof_app.model.updateWidthGeneral
import com.pavlig43.roof_app.ui.calculation_tile_4scat.SaveNameFile
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFilePDF
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ShapesViewModel @Inject constructor() : ViewModel() {

    private val _shapesScreenState: MutableStateFlow<ShapesScreenState> =
        MutableStateFlow(ShapesScreenState.ShapesMain)
    val shapesScreenState = _shapesScreenState.asStateFlow()


    /**
     * Список страниц ПДФ файла для отображения на экране
     */
    private val _listBitmap:MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()

    /**
     * Когда пользователь Выбрал все параметры, сначала создается ПДФ файл, который рендерится , а потом только показывается на экране
     */
    private val _pdfFile:MutableStateFlow<File?> = MutableStateFlow(null)

    /**
     * Имя файла, с которым можно сохранить документ ПДФ,
     * в [checkName] проверяет есть ли уже такое в хранилище
     */
    private val  _saveNameFile:MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    private val _sheet:MutableStateFlow<Sheet> = MutableStateFlow(Sheet())
    val sheet = _sheet.asStateFlow()





    fun moveToShape(shapeName:String){
        when(shapeName){
            "Треугольник"-> _shapesScreenState.value = ShapesScreenState.Triangle
            "4-угольник" ->_shapesScreenState.value = ShapesScreenState.Quadrilateral
        }
    }

    fun returnCalculateTriangleScreen(){
        _shapesScreenState.value = ShapesScreenState.Triangle
    }

    fun changeWidthOfSheet(newWidth: Float){
        _sheet.value = _sheet.value.updateWidthGeneral(newWidth)
    }
    fun changeOverlap(newOverlap:Float){
        _sheet.value = _sheet.value.updateOverlap(newOverlap)
    }

    fun changeMultiplicity(newMultiplicity:Float){
       _sheet.update { it.updateMultiplicity(newMultiplicity) }
    }

    fun openDocument(file: File){
        _pdfFile.value = file
        _listBitmap.value = renderPDF(_pdfFile.value!!,viewModelScope)
        _shapesScreenState.value = ShapesScreenState.LoadDocumentImage
    }
    fun checkName(newName:String,context: Context){
        val isValid = checkSaveName(newName, context)
        _saveNameFile.update { it.copy(name = newName,isValid=isValid) }
    }
    fun saveFile(context: Context){
        saveFilePDF(context, _pdfFile.value, saveNameFile = _saveNameFile.value.name)
    }
    fun shareFile(context: Context){
        if (_pdfFile.value !=null){
            sharePDFFile(context,_pdfFile.value!!)
        }

    }
}

sealed class ShapesScreenState {
    data object ShapesMain : ShapesScreenState()
    data object Triangle : ShapesScreenState()
    data object LoadDocumentImage:ShapesScreenState()
    data object Quadrilateral:ShapesScreenState()
}