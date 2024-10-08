package com.pavlig43.roofapp.ui.shapes

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam

import com.pavlig43.roofapp.model.updateSheetParams

import com.pavlig43.roofapp.ui.calculationTile4scat.SaveNameFile
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
class ShapesViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _shapesScreenState =
            MutableStateFlow<ShapesScreenState>(ShapesScreenState.ShapesMain)
        val shapesScreenState = _shapesScreenState.asStateFlow()

    /**
     * Когда пользователь Выбрал все параметры, сначала создается ПДФ файл, который рендерится ,
     * а потом только показывается на экране
     */
    private val _pdfFile: MutableStateFlow<File?> = MutableStateFlow(null)
    private val pdfFile: StateFlow<File?> = _pdfFile.asStateFlow()

        /**
         * Список страниц ПДФ файла для отображения на экране
         */
        private val pathURI = _pdfFile.
        filterNotNull().
        map { file->
            Log.d("URI",file.toUri().toString())
            file.toUri() }.
        stateIn(viewModelScope, SharingStarted.Lazily,null)

    val pdfReaderState: StateFlow<VerticalPdfReaderState?> = pathURI.filterNotNull().map { uri->
        VerticalPdfReaderState(ResourceType.Local(uri))
    }.stateIn(viewModelScope, SharingStarted.Lazily,null)



        /**
         * Имя файла, с которым можно сохранить документ ПДФ,
         * в [checkName] проверяет есть ли уже такое в хранилище
         */
        private val _saveNameFile: MutableStateFlow<SaveNameFile> = MutableStateFlow(SaveNameFile())
        val saveNameFile = _saveNameFile.asStateFlow()

        private val _sheet: MutableStateFlow<Sheet> = MutableStateFlow(Sheet())
        val sheet = _sheet.asStateFlow()

        fun moveToShape(shapeName: Shapes) {
            _shapesScreenState.value =
                when (shapeName) {
                    Shapes.Triangle -> ShapesScreenState.Triangle
                    Shapes.Quadrilateral -> ShapesScreenState.Quadrilateral
                }
        }

        fun returnCalculateTriangleScreen() {
            _shapesScreenState.value = ShapesScreenState.Triangle
        }


    fun updateSheetParams(sheetParam: SheetParam){
        _sheet.update { it.updateSheetParams(sheetParam) }
    }

        fun openDocument(file: File) {
            _pdfFile.value = file

            _shapesScreenState.value = ShapesScreenState.LoadDocumentImage
        }

        fun checkName(
            newName: String,
            context: Context,
        ) {
            val isValid = context.checkSaveName(newName)
            _saveNameFile.update { it.copy(name = newName, isValid = isValid) }
        }

        fun saveFile(context: Context) {
            context.saveFilePDF(_pdfFile.value!!, saveNameFile = _saveNameFile.value.name)
        }

        fun shareFile() {
            pdfFile.value?.let { pdfFile -> context.sharePDFFile( pdfFile) }
        }
    }

sealed class ShapesScreenState {
    data object ShapesMain : ShapesScreenState()

    data object Triangle : ShapesScreenState()

    data object LoadDocumentImage : ShapesScreenState()

    data object Quadrilateral : ShapesScreenState()
}
