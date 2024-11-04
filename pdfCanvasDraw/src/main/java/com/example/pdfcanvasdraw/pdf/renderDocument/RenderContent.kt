package com.example.pdfcanvasdraw.pdf.renderDocument

import android.graphics.pdf.PdfDocument
import com.example.pdfcanvasdraw.pdf.page.PageRenderer

class PdfBuilder {
    suspend fun createPdf(
        listOfPageRenderer: List<PageRenderer>
    ): PdfDocument {
        val pdf = PdfDocument()

        listOfPageRenderer.mapIndexed { index, pageRenderer ->
            pageRenderer.renderPage(pdf, index + 1)
        }


//        pdf.close()
        return pdf
    }
}

