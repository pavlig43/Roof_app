package com.pavlig43.roofapp.ui.saveDocuments

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roofapp.utils.renderPDF
import com.pavlig43.roofapp.utils.sharePDFFile
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
import javax.inject.Inject

@HiltViewModel
class ListSaveDocumentsViewModel
    @Inject
    constructor(
        @ApplicationContext val context: Context,
    ) : ViewModel() {
        private val _screensSaveDocumentsState: MutableStateFlow<ScreensSaveDocumentsState> =
            MutableStateFlow(ScreensSaveDocumentsState.ListSaveDocumentsState)
        val screensSaveDocumentsState = _screensSaveDocumentsState.asStateFlow()

        private val _listSaveDocument: MutableStateFlow<List<Document>> = MutableStateFlow(emptyList())
        val listSaveDocument = _listSaveDocument.asStateFlow()

//
        private val _documentState = MutableStateFlow<Document?>(null)
    val pdfReaderState: StateFlow<VerticalPdfReaderState?> = _documentState.filterNotNull().map { doc->
        VerticalPdfReaderState(ResourceType.Local(doc.pdf.toUri()))
    }.stateIn(viewModelScope, SharingStarted.Lazily,null)


        init {
            getListSaveDocument()
        }

        fun returnScreenListDocument() {
            _screensSaveDocumentsState.value = ScreensSaveDocumentsState.ListSaveDocumentsState
        }

        private fun getListSaveDocument() {
            viewModelScope.launch(Dispatchers.IO) {
                val files =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.listFiles() ?: run {
                        _listSaveDocument.value = emptyList()
                        return@launch
                    }

                _listSaveDocument.value =
                    files
                        .groupBy { it.nameWithoutExtension }
                        .mapNotNull { (name, values) ->
                            val pdf = values.find { it.extension == "pdf" } ?: return@mapNotNull null
                            if (name != "roof") Document(name, pdf) else null
                        }
            }
        }

        fun openDocument(document: Document) {
            _documentState.update { document }
            _screensSaveDocumentsState.value = ScreensSaveDocumentsState.DrawDocumentState
        }

        fun shareFile(
            document: Document,
        ) {
            context.sharePDFFile( document.pdf)
        }

        fun deleteFile(

            document: Document,
        ) {
            _listSaveDocument.update { list -> list.filterNot { it.name == document.name } }
            try {
                val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, document.pdf.name)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                Log.d("deleteLog", "$e")
            }
        }
    }

sealed class ScreensSaveDocumentsState {
    data object ListSaveDocumentsState : ScreensSaveDocumentsState()

    data object DrawDocumentState : ScreensSaveDocumentsState()
}

data class Document(
    val name: String,
    val pdf: File,
)
