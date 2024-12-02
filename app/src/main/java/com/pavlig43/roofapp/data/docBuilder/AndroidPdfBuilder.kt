package com.pavlig43.roofapp.data.docBuilder

import android.graphics.pdf.PdfDocument
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.DocBuilder
import com.pavlig43.pdfcanvasdraw.implementationCore.pdf.pdfAndroid.PdfAndroidCanvas
import com.pavlig43.roofapp.data.AndroidFileStorageRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Класс AndroidPdfBuilder реализует интерфейс DocBuilder и предоставляет возможность создавать
 * PDF-документ на основе списка  страниц. В его конструктор передаётся объект
 * AndroidFileStorageRepository, который используется для сохранения сгенерированного файла.
 * Основной метод [createAndGetFileName] принимает список объектов [PageRenderer], каждый из которых
 * отвечает за рендеринг одной страницы. Метод создаёт экземпляр PdfDocument и для каждой страницы
 * генерирует объект PdfDocument.PageInfo, определяющий размеры страницы и её индекс. Затем открывается
 * страница с помощью PdfDocument.startPage, и её содержимое рендерится через вызов метода [renderPage]
 * у PageRenderer. После завершения рендеринга текущая страница добавляется в PDF с помощью
 * PdfDocument.finishPage. По завершении рендеринга всех страниц PDF-документ сохраняется с
 * использованием метода saveAndGetFileName из репозитория. Этот метод принимает лямбда-функцию,
 * которая записывает содержимое PDF в выходной поток, закрывает документ и возвращает имя файла.
 * После успешного выполнения метода возвращается имя сохранённого файла. Файд приходит пустой из репозитория
 */
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
