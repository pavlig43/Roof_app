package com.pavlig43.roofapp.ui.changeRoofInfoText

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pavlig43.roofapp.FILE_NAME
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TextInPdfViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
) : ViewModel() {
    private val filePath: String = checkNotNull(saveStateHandle[FILE_NAME])

    val pdfDoc = PDDocument.load(File(filePath))
    val text = pdfDoc.use {
        val textStripper = PDFTextStripper()
        textStripper.startPage = it.numberOfPages
        textStripper.endPage = it.numberOfPages
        textStripper.getText(pdfDoc)
    }

    init {
        Log.d("textChange", text)
    }
}

data class Page(
    val number: Int,
    val text: String,
)
