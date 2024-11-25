package com.pavlig43.roofapp.ui.pdfImage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState

@Composable
fun PDFView(
    pdfState: VerticalPdfReaderState,
    modifier: Modifier = Modifier,
) {
    Box {
        VerticalPDFReader(
            state = pdfState,
            modifier =
            modifier
                .fillMaxSize(),
        )
    }
}
