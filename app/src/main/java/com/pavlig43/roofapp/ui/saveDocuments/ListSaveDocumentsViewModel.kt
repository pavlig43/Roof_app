package com.pavlig43.roofapp.ui.saveDocuments

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class ListSaveDocumentsViewModel
    @Inject
    constructor(
        @ApplicationContext val context: Context,
    ) : ViewModel() {
        private val _screensSaveDocumentsState =
            MutableStateFlow<ScreensSaveDocumentsState>(ScreensSaveDocumentsState.ListSaveDocumentsState)
        val screensSaveDocumentsState = _screensSaveDocumentsState.asStateFlow()

        private val documentState = MutableStateFlow<Document?>(null)

        val pdfReaderState: StateFlow<VerticalPdfReaderState?> =
            documentState
                .filterNotNull()
                .map { doc ->
                    VerticalPdfReaderState(ResourceType.Local(doc.pdf.toUri()))
                }.stateIn(viewModelScope, SharingStarted.Lazily, null)

        fun returnScreenListDocument() {
            _screensSaveDocumentsState.value = ScreensSaveDocumentsState.ListSaveDocumentsState
        }

        val listSaveDocument: StateFlow<SnapshotStateList<Document>> =
            flow {
                loadListSaveDocument().run { emit(this) }
            }.stateIn(viewModelScope, SharingStarted.Lazily, mutableStateListOf())

        private fun loadListSaveDocument(): SnapshotStateList<Document> {
            val files =
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.listFiles()
                    ?: emptyArray()

            return files
                .groupBy { it.nameWithoutExtension }
                .mapNotNull { (name, values) ->
                    val pdf = values.find { it.extension == "pdf" } ?: return@mapNotNull null
                    val document =
                        pdf.let { _ -> takeIf { name != "roof" }?.let { Document(name, pdf) } }
                    document
                }.toMutableStateList()
        }

        fun openDocument(document: Document) {
            documentState.update { document }
            _screensSaveDocumentsState.value = ScreensSaveDocumentsState.DrawDocumentState
        }

        fun shareFile(document: Document) {
            context.sharePDFFile(document.pdf)
        }

        fun deleteFile(document: Document) {
            try {
                val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, document.pdf.name)
                if (file.exists()) {
                    file.delete()
                }
                listSaveDocument.value.removeIf { file.name == it.pdf.name }
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
