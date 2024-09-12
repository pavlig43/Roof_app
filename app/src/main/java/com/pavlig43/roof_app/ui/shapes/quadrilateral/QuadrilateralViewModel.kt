package com.pavlig43.roof_app.ui.shapes.quadrilateral

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.ui.calculation_tile_4scat.SaveNameFile
import com.pavlig43.roof_app.utils.checkSaveName
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.saveFilePDF
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class QuadrilateralViewModel @Inject constructor() : ViewModel() {

    private val _screenState:MutableStateFlow<QuadroScreenState> = MutableStateFlow(QuadroScreenState.ChoiceState)

    val screenState = _screenState.asStateFlow()

    private val _geometry4SideShape: MutableStateFlow<Geometry4SideShape> =
        MutableStateFlow(Geometry4SideShape())

    val geometryShape = _geometry4SideShape.asStateFlow()

    /**
     * Текущая точка в системе координат, у которой пользователь может менять координаты
     * у нижней левой точки всегда координаты (0,0) пользователь не может ее менять
     */

    private val _currentDotName = MutableStateFlow(_geometry4SideShape.value.leftBottom.name)

    /**
     * Текущая точка , которая выбранна пользователем
     */
    val currentDot:StateFlow<Dot> = combine(_geometry4SideShape,_currentDotName){
        _,_-> changeCurrentDot()}
    .stateIn(viewModelScope, SharingStarted.Eagerly,_geometry4SideShape.value.leftBottom)

    /**
     * Переменная, которая показывает является ли 4хугольник правильным выпуклым
     * не уверен , что на самом деле описывает все случаи
     * TODO() нужно посмотреть законы в геометрии
     */
    val isValid = _geometry4SideShape.map { newShape->
        shapeIsValid(newShape)

    }
        .stateIn(viewModelScope, SharingStarted.Eagerly,false)

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


    private fun shapeIsValid(geometry4SideShape: Geometry4SideShape): Boolean {
        return geometry4SideShape.leftTop.distanceX>0f &&
        geometry4SideShape.rightTop.distanceX>0f &&
        geometry4SideShape.rightBottom.distanceY!=0f

    }

    fun returnCalculateScreen(){
        _screenState.value = QuadroScreenState.ChoiceState
    }

    fun changeCurrentDotName(dotName4Side: DotName4Side){
        _currentDotName.value = dotName4Side
    }

    private fun changeCurrentDot(): Dot {
        val dots = listOf(
            _geometry4SideShape.value.leftBottom,
            _geometry4SideShape.value.leftTop,
            _geometry4SideShape.value.rightTop,
            _geometry4SideShape.value.rightBottom
        )
        return dots.first { it.name == _currentDotName.value }
    }

    fun changeParamsDot(dot: Dot) {

        when (dot.name) {
            DotName4Side.LEFTTOP -> _geometry4SideShape.update { it.copy(leftTop = dot) }
            DotName4Side.LEFTBOTTOM -> _geometry4SideShape.update { it.copy(leftBottom = dot) }
            DotName4Side.RIGHTBOTTOM -> _geometry4SideShape.update { it.copy(rightBottom = dot) }
            DotName4Side.RIGHTTOP -> _geometry4SideShape.update { it.copy(rightTop = dot) }
        }
    }

    /**
     * если все параметры валидны, то кнопка показать результат активна, после нажатия
     * создает и сохраняется ПДФ , затем открывает его и рендерит в bitmap
     */
    fun openDocument(context: Context){
        _pdfFile.value = pdfResult4Side(context,_geometry4SideShape.value,)
        _listBitmap.value = renderPDF(_pdfFile.value!!,viewModelScope)
        _screenState.value = QuadroScreenState.PdfResult
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





data class Geometry4SideShape(
    val leftBottom: Dot = Dot(name = DotName4Side.LEFTBOTTOM, ),
    val leftTop: Dot = Dot(name = DotName4Side.LEFTTOP, canMinusY = true, distanceX = 500f, distanceY = 80f),
    val rightTop: Dot = Dot(name = DotName4Side.RIGHTTOP, canMinusY = true, distanceX = 700f, distanceY = 220f),
    val rightBottom: Dot = Dot(name = DotName4Side.RIGHTBOTTOM, canMinusX = true, distanceX = -100f, distanceY = 350f),




    )

sealed class QuadroScreenState{
    data object ChoiceState:QuadroScreenState()
    data object PdfResult:QuadroScreenState()
}