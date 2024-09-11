package com.pavlig43.roof_app.ui.save_documents

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roof_app.utils.renderPDF
import com.pavlig43.roof_app.utils.sharePDFFile
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ListSaveDocumentsViewModel @Inject constructor(
    @ApplicationContext val context : Context
):ViewModel() {

    private val _screensSaveDocumentsState:MutableStateFlow<ScreensSaveDocumentsState> = MutableStateFlow(ScreensSaveDocumentsState.ListSaveDocumentsState)
    val screensSaveDocumentsState = _screensSaveDocumentsState.asStateFlow()

    private val _listSaveDocument:MutableStateFlow<List<Document>> = MutableStateFlow(listOf())
    val listSaveDocument = _listSaveDocument.asStateFlow()

    private val _listBitmap:MutableStateFlow<List<Bitmap>> = MutableStateFlow(listOf())
    val listBitmap = _listBitmap.asStateFlow()
    init {
        getListSaveDocument()
    }

    fun returnScreenListDocument(){
        _screensSaveDocumentsState.value = ScreensSaveDocumentsState.ListSaveDocumentsState
    }

     private fun getListSaveDocument(){
        val directory = context.getExternalFilesDir(null)
        val groupListSaveDocument:List<Document> = directory?.listFiles()?.toList()?.groupBy { it.nameWithoutExtension }?.map {(name,values)->
            val pdf = values.find { it.extension == "pdf"}

            if (pdf!=null && name != "roof"){
                Document(name,pdf, )
            }
            else{null}
        }?.filterNotNull()?: listOf()

        _listSaveDocument.value = groupListSaveDocument
    }
    fun openDocument(document: Document){

        if (document.pdf !=null){
            _listBitmap.value = renderPDF(document.pdf,viewModelScope)
           _screensSaveDocumentsState.value = ScreensSaveDocumentsState.DrawDocumentState
        }
    }
    fun shareFile(context: Context,document: Document){
        sharePDFFile(context,document.pdf)
    }

    fun deleteFile(
        context: Context,
        document: Document){
        _listSaveDocument.update { list -> list.filterNot { it.name == document.name } }
        try {
            val directory = context.getExternalFilesDir(null)
            val file =File(directory,document.pdf.name)
            if(file.exists()){
                file.delete()
            }
        }
        catch (e:Exception){
            Log.d("deleteLog","$e")
        }

    }
}

sealed class ScreensSaveDocumentsState{
    data object ListSaveDocumentsState:ScreensSaveDocumentsState()
    data object DrawDocumentState:ScreensSaveDocumentsState()
}
data class Document(
    val name:String,
    val pdf:File,

)