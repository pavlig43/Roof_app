package com.pavlig43.roofapp.ui.pdfImage

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roofapp.FILE_NAME
import com.pavlig43.roofapp.IS_CONSTRUCTOR
import com.pavlig43.roofapp.WHILE_SUBSCRIBED
import com.pavlig43.roofapp.data.fileStorage.AndroidFileStorageRepository
import com.pavlig43.roofapp.data.shapeMulti.ShapeMultiProvider
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PDFImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AndroidFileStorageRepository,
    private val shapeMultiProvider: ShapeMultiProvider,
) : ViewModel() {

    private val filePath: String = checkNotNull(savedStateHandle[FILE_NAME])
    val isConstructor: Boolean =
        savedStateHandle.get<String?>(IS_CONSTRUCTOR)
            ?.toBooleanStrictOrNull() ?: true

    private val fileFlow = repository.loadFile(filePath)

    private val file = fileFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED),
            File("")
        )

    /**
     * Список страниц ПДФ файла для отображения на экране
     */
    val verticalPdfReaderState = fileFlow.map { file ->
        val uri = file.toUri()
        VerticalPdfReaderState(ResourceType.Local(uri))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _saveFileName = MutableStateFlow("")
    val saveFileName = _saveFileName.asStateFlow()
    fun changeName(newName: String) {
        _saveFileName.update { newName }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val isValidName = _saveFileName.flatMapLatest {
        repository.checkSaveName(it)
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun shareFile() {
        repository.shareFile(file.value)
    }

    fun saveFile() {
        shapeMultiProvider.clear()
        viewModelScope.launch {
            repository.reNameFile(
                file.value,
                _saveFileName.value
            )
        }
    }
}
