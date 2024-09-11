package com.pavlig43.roof_app.ui.shapes.quadrilateral

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.ui.calculation_tile_4scat.SaveNameFile
import com.pavlig43.roof_app.ui.shapes.ShapesScreenState
import com.pavlig43.roof_app.ui.shapes.triangle.triangleResult
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFileUtils
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class QuadrilateralViewModel @Inject constructor() : ViewModel() {

    private val _screenState:MutableStateFlow<QuadroScreenState> = MutableStateFlow(QuadroScreenState.ChoiceState)

    val screenState = _screenState.asStateFlow()

    private val _geometryShape: MutableStateFlow<GeometryShape> =
        MutableStateFlow(GeometryShape())

    val geometryShape = _geometryShape.asStateFlow()
    private val _currentDot: MutableStateFlow<Dot> =
        MutableStateFlow(_geometryShape.value.leftBottom)

    val currentDot = _currentDot.asStateFlow()

    private var _currentDotName: DotName = DotName.LEFTBOTTOM

    val isValid = _geometryShape.map {
        shapeIsValid(it)
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly,false)

    private val _listBitmap:MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()

    private val _pdfFile:MutableStateFlow<File?> = MutableStateFlow(null)

    private val  _saveNameFile:MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
    val saveNameFile = _saveNameFile.asStateFlow()

    init {
        viewModelScope.launch {
            _geometryShape.collect{newShape->
                updateCurrentDot(_currentDotName,newShape)
            }
        }


    }
    private fun shapeIsValid(geometryShape: GeometryShape): Boolean {
        return geometryShape.leftTop.distanceX>0 &&
        geometryShape.rightTop.distanceX>0 &&
        geometryShape.rightBottom.distanceY!=0

    }

    fun returnCalculateScreen(){
        _screenState.value = QuadroScreenState.ChoiceState
    }


    fun changeCurrentDot(dotName: DotName){
        _currentDotName = dotName
        updateCurrentDot(_currentDotName,_geometryShape.value)
    }
    private fun updateCurrentDot(dotName: DotName, newShape: GeometryShape) {
        _currentDot.value = when (dotName) {
            DotName.LEFTTOP -> newShape.leftTop
            DotName.LEFTBOTTOM -> newShape.leftBottom
            DotName.RIGHTBOTTOM -> newShape.rightBottom
            DotName.RIGHTTOP -> newShape.rightTop
        }
    }


    fun changeParamsDot(dot: Dot) {
        Log.d("dotcurrent", _currentDot.value.toString())
        Log.d("dotA", dot.toString())
        when (dot.name) {
            DotName.LEFTTOP -> _geometryShape.update { it.copy(leftTop = dot) }
            DotName.LEFTBOTTOM -> _geometryShape.update { it.copy(leftBottom = dot) }
            DotName.RIGHTBOTTOM -> _geometryShape.update { it.copy(rightBottom = dot) }
            DotName.RIGHTTOP -> _geometryShape.update { it.copy(rightTop = dot) }
        }
        Log.d("_dotgeometricShape", _geometryShape.value.leftTop.toString())


    }

    fun openDocument(context: Context){
        _pdfFile.value = quadroPdfResult(context,_geometryShape.value,)
        _listBitmap.value = renderPDF(_pdfFile.value!!,viewModelScope)
        _screenState.value = QuadroScreenState.PdfResult
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

enum class DotName {
    LEFTBOTTOM, LEFTTOP, RIGHTBOTTOM, RIGHTTOP
}

data class Dot(
    val name: DotName,
    val distanceX: Int = 0,
    val distanceY: Int = 0,
    val canMinusX: Boolean = false,
    val canMinusY: Boolean = false
)

data class GeometryShape(
    val leftBottom: Dot = Dot(name = DotName.LEFTBOTTOM, ),
    val leftTop: Dot = Dot(name = DotName.LEFTTOP, canMinusY = true, distanceX = 500, distanceY = 80),
    val rightTop: Dot = Dot(name = DotName.RIGHTTOP, canMinusY = true, distanceX = 700, distanceY = 220),
    val rightBottom: Dot = Dot(name = DotName.RIGHTBOTTOM, canMinusX = true, distanceX = -100, distanceY = 350),




)

sealed class QuadroScreenState{
    data object ChoiceState:QuadroScreenState()
    data object PdfResult:QuadroScreenState()
}