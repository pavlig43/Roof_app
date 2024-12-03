package com.pavlig43.roofapp.ui.saveDocuments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roofapp.data.fileStorage.AndroidFileStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class ListSaveDocumentsViewModel
@Inject
constructor(
    private val repository: AndroidFileStorageRepository,
) : ViewModel() {

    val listSaveDocuments = repository.listOfFiles.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        listOf()
    )

    fun shareFile(file: File) {
        repository.shareFile(file)
    }

    fun deleteFile(file: File) {
        viewModelScope.launch {
            repository.delete(file)
        }
    }
}
