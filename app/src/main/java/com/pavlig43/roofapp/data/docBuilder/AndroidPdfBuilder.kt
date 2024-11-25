package com.pavlig43.roofapp.data.docBuilder

import android.graphics.pdf.PdfDocument
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer
import com.example.pdfcanvasdraw.implementationCore.pdf.DocBuilder
import com.example.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.PdfAndroidCanvas
import com.pavlig43.roofapp.data.AndroidFileStorageRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidPdfBuilder @Inject constructor(
    private val repository: AndroidFileStorageRepository
) :
    DocBuilder {

    override suspend fun createAndGetFileName(listOfPageRenderer: List<PageRenderer>): String {
        val openPdf = PdfDocument()
        listOfPageRenderer.forEachIndexed { index, pageRenderer ->
            val pageInfo =
                PdfDocument.PageInfo.Builder(
                    pageRenderer.pageConfig.x,
                    pageRenderer.pageConfig.y,
                    index + 1,
                ).create()
            val page = openPdf.startPage(pageInfo)
            val canvas = PdfAndroidCanvas(page.canvas)

            pageRenderer.renderPage(canvas)

            openPdf.finishPage(page)
        }

        val fileName = repository.saveAndGetFileName { dispatcher, file ->
            withContext(dispatcher) {
                file.outputStream().use { openPdf.writeTo(it) }
                openPdf.close()
                file.name
            }
        }
        return fileName
    }
}
