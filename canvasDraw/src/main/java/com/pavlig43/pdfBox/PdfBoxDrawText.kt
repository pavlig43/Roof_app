package com.pavlig43.pdfBox

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.TEXT_SIZE_MEDIUM_PLUS
import com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.drawText.AddVerticalPaddingForLineText
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class PdfBoxDrawText(
    private val filePath: String,
    fontInputStream: InputStream,
    private val dispatcher: CoroutineDispatcher,
    private val markInfo: String = "",
    private val textSize: Float = TEXT_SIZE_MEDIUM_PLUS,
) {
    @Suppress("MagicNumber")
    private val markOffset = PointF(15f, 15f)

    @Suppress("MagicNumber")
    private val startOffset = PointF(25f, 25f)

    @Suppress("MagicNumber")
    private val spaceLine = 25f

    private val pdfDoc = PDDocument.load(File(filePath))


    private var firstIndexPageMark: Int = -1

    private val font = PDType0Font.load(pdfDoc, fontInputStream)

    private suspend fun removePages() {
        withContext(dispatcher) {
            val reversedRangeIndexPageWithMark =
                pdfDoc.numberOfPages downTo firstIndexPageMark

            reversedRangeIndexPageWithMark.forEach { pageIndex ->
                pdfDoc.removePage(pageIndex - 1)
            }
        }
    }


    suspend fun reWrite(newText: String) {
        withContext(dispatcher) {
            if (firstIndexPageMark == -1) {
                return@withContext
            } else {
                removePages()
                val countRowStringOnOnePage = getCountStringRowOnOnePage(
                    startPaddingY = startOffset.y,
                )
                newText.split("\n").chunked(countRowStringOnOnePage).forEach {
                    drawTextOnPdfBoxPage(
                        it,
                    )
                }
            }
            pdfDoc.save(File(filePath))
        }
    }

    suspend fun getStartAllTextWithMarkInfo(): String {
        return withContext(dispatcher) {
            (pdfDoc.numberOfPages downTo 1).mapNotNull { index ->
                val textStripper = PDFTextStripper()
                textStripper.startPage = index
                textStripper.endPage = index
                val pageText = textStripper.getText(pdfDoc)
                pageText.takeIf { it.contains(markInfo) }?.let {
                    firstIndexPageMark = index
                    pageText.removePrefix(markInfo).trim()
                }
            }.reversed().joinToString()
        }


    }

    private fun drawTextOnPdfBoxPage(
        text: List<String>,
    ) {
        val rowSpacing = AddVerticalPaddingForLineText(startOffset.y, spaceLine)
        val page = PDPage(PDRectangle.A4)
        val heightPage = page.mediaBox.height
        val widthPage = page.mediaBox.width

        pdfDoc.addPage(page)
        PDPageContentStream(pdfDoc, page).use { contentStream ->
            text.forEach {
                contentStream.setFont(font, textSize)
                contentStream.setLeading(spaceLine)
                contentStream.beginText()
                contentStream.newLineAtOffset(startOffset.x, heightPage - rowSpacing.y)
                contentStream.showText(it)
                contentStream.endText()
                rowSpacing.addTransferText()
            }
            contentStream.setFont(font, textSize)
            contentStream.setLeading(spaceLine)
            contentStream.beginText()
            contentStream.newLineAtOffset(widthPage - markOffset.x, heightPage - markOffset.y)
            contentStream.showText(markInfo)
            contentStream.endText()
        }
    }

    @Suppress("MagicNumber")
    private fun getCountStringRowOnOnePage(
        startPaddingY: Float,
    ): Int {
        val usableHeight = PDRectangle.A4.height - 2 * startPaddingY
        val lineText = (textSize + spaceLine) / 1.8
        return (usableHeight / lineText).toInt()
    }

    fun getFilePath() = filePath
}
