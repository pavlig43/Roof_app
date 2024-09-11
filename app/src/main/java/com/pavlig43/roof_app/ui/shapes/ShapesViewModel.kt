package com.pavlig43.roof_app.ui.shapes

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.model.ShapeSide
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.Triangle
import com.pavlig43.roof_app.ui.calculation_tile_4scat.SaveNameFile
import com.pavlig43.roof_app.ui.shapes.triangle.triangleResult
import com.pavlig43.roof_app.utils.calculateCmInM
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFileUtils
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

    private val _listBitmap:MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()

    private val _pdfFile:MutableStateFlow<File?> = MutableStateFlow(null)

    private val  _saveNameFile:MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    private val _sheet:MutableStateFlow<Sheet> = MutableStateFlow(Sheet())
    val sheet = _sheet.asStateFlow()

    private val _triangle:MutableStateFlow<Triangle> = MutableStateFlow(Triangle())
    val triangle = _triangle.asStateFlow()

    fun moveToShape(shapeName:String){
        when(shapeName){
            "Треугольник"-> _shapesScreenState.value = ShapesScreenState.Triangle
            "4-угольник" ->_shapesScreenState.value = ShapesScreenState.Quadrilateral
        }
    }

    fun returnCalculateTriangleScreen(){
        _shapesScreenState.value = ShapesScreenState.Triangle
    }
    fun changeWidthOfSheet(newWidth: String){
        _sheet.update { it.copy(widthGeneral = calculateCmInM(newWidth)) }
    }
    fun changeOverlap(newOverlap:String){
        _sheet.update { it.copy(overlap = calculateCmInM(newOverlap)) }
    }

    fun changeTriangle(shapeSide: ShapeSide){
        when(shapeSide.name){
            "a"-> _triangle.update { it.copy(a=shapeSide) }
            "b"-> _triangle.update { it.copy(b=shapeSide) }
            "c"-> _triangle.update { it.copy(c=shapeSide) }

        }
    }
    fun openDocument(context: Context){
        _pdfFile.value = triangleResult(context,_triangle.value,_sheet.value)
        _listBitmap.value = renderPDF(_pdfFile.value!!,viewModelScope)
        _shapesScreenState.value = ShapesScreenState.LoadDocumentImage
    }
    fun checkName(newName:String,context: Context){
        val isValid = checkSaveName(newName, context)
        _saveNameFile.update { it.copy(name = newName,isValid=isValid) }
    }
    fun saveFile(context: Context){
        saveFileUtils(context, _pdfFile.value, saveNameFile = _saveNameFile.value.name)
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