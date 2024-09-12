package com.pavlig43.roof_app.utils

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * Функция из файла ПДФ рендерит список bitmap, который потом используется для отображения на экране
 * в composable Image в  LazyColumn
 */
fun renderPDF(
    file: File,
    coroutineScope: CoroutineScope,
): MutableList<Bitmap> {

    val bitmaps: MutableList<Bitmap> = mutableListOf()
    coroutineScope.launch {
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRender = PdfRenderer(fileDescriptor)


        for (pageNumber in 0 until pdfRender.pageCount) {

            val page = pdfRender.openPage(pageNumber)
            // ширина и высота делится на 2, так как по умолчанию у рендеренной картинки низкое качество изображения
            val bitmap =
                Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
            bitmaps.add(bitmap)
            page.close()

        }
    }
    return bitmaps
}