package com.pavlig43.roofapp.ui.changeInfoText

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.pdfBox.PdfBoxDrawText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextInPdfViewModel @Inject constructor(
    private val pdfBoxDrawText: PdfBoxDrawText
) : ViewModel() {

    private val _allText = MutableStateFlow("")

    val allText = _allText.asStateFlow()

    init {
        getStartText()
    }

    private fun getStartText() {
        viewModelScope.launch {
            _allText.update { pdfBoxDrawText.getStartAllTextWithMarkInfo() }
        }
    }

    fun changePageText(newText: String) {
        _allText.update { newText }
    }

    private fun saveDoc() {
        viewModelScope.launch {
            pdfBoxDrawText.reWrite(_allText.value)
        }
    }

    fun showResult(showResult: (String) -> Unit) {
        saveDoc()
        showResult(pdfBoxDrawText.getFilePath())
    }
}
