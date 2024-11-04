package com.pavlig43.roofapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import androidx.core.content.FileProvider
import com.example.pdfcanvasdraw.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.pdf.model.PageConfig
import com.example.pdfcanvasdraw.pdf.page.AndroidCanvas
import com.pavlig43.roof_app.BuildConfig
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.A4X
import com.pavlig43.roofapp.A4Y
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.ui.shapes.quadrilateral.Geometry4SideShape
import com.pavlig43.roofapp.ui.shapes.quadrilateral.QuadroPDF
import com.pavlig43.roofapp.ui.shapes.triangle.GeometryTriangle3SideShape
import com.pavlig43.roofapp.ui.shapes.triangle.TrianglePDF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Запускает окно, чтобы поделиться файлом в приложении на выбор
 */
fun Context.sharePDFFile(pdfFile: File) {
    val uri =
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            pdfFile,
        )
    val sharedIntent =
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
    startActivity(Intent.createChooser(sharedIntent, getString(R.string.send)))
}

fun Context.deleteFile(pdfFile: File) {
    val directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, pdfFile.name)
    if (file.exists()) {
        file.delete()
    }
}

/**
 * Сохраняет файл с переданным именем в хранилище, файл можно посмотреть только из приложения,
 * доступа к нему из других приложений нет
 */
suspend fun Context.saveFilePDF(
    pdfFile: File,
    saveNameFile: String,
) {
    withContext(Dispatchers.IO) {
        val filePDF =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$saveNameFile.pdf")
        FileOutputStream(filePDF).use { outputStream ->
            pdfFile.inputStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}

/**
 * Проверяет , есть ли документ с переданным именем в списке документов в хранилище данного приложения для ПДФ
 */
fun Context.checkSaveName(newName: String): Boolean {
    val directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val listFiles = directory?.listFiles() ?: return false
    val listOfFiles =
        listFiles
            .toList()
            .map {
                it.name
                    .split("/")
                    .last()
                    .replace(".pdf", "")
            }
    return newName !in listOfFiles && newName.isNotBlank()
}

suspend fun PdfDocument.createFile(context: Context): File =

    withContext(Dispatchers.IO) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "roof.pdf")
        file.outputStream().use { this@createFile.writeTo(it) }
        this@createFile.close()
        file
    }

/**
 * В ПДФ документ вставляет рисунок 4х-угольника уже заполненный листами металла
 * и возвращает длины всех листов
 * [single] - если одиночная фигура , то добавляется лист с описанием этого 4х-угольника(размеры сторон, что_нибудь еще)
 */
suspend fun PdfDocument.pdfResult4Side(
    context: Context,
    geometry4SideShape: Geometry4SideShape,
    pageNumber: Int,
    single: Boolean = false,
    sheet: Sheet,
): MutableList<Sheet> =
    withContext(Dispatchers.IO) {
        val quadroPDF = QuadroPDF(geometry4SideShape, sheet)

        val pageInfo = PageInfo.Builder(A4X, A4Y, pageNumber).create()
        val page = this@pdfResult4Side.startPage(pageInfo)
        val canvas = AndroidCanvas(page.canvas)

//        quadroPDF.ruler(canvas)
//        quadroPDF.ruler(canvas)
        quadroPDF.drawQuadro()
        quadroPDF.sheetOnQuadro()
        this@pdfResult4Side.finishPage(page)
        val listOfSheet = quadroPDF.getLstOfSheet()
        if (single) {
            val otherParams = quadroPDF.getOtherParams()
            this@pdfResult4Side.addInfo(
                context,
                listOfSheet,
                fullRoof = false,
                pageNumber = pageNumber + 1,
                otherParams = otherParams,
            )
        }
        listOfSheet
    }

/**
 * В ПДФ документ вставляет рисунок 3х-угольника уже заполненный листами металла
 * и возвращает длины всех листов
 * [single] - если одиночная фигура , то добавляется лист с описанием этого триугольника(размеры сторон, что_нибудь еще)
 */
suspend fun PdfDocument.pdfResult3SideTriangle(
    context: Context,
    geometryTriangle3SideShape: GeometryTriangle3SideShape,
    pageNumber: Int,
    single: Boolean = false,
    sheet: Sheet,
): MutableList<Sheet> =
    withContext(Dispatchers.IO) {
        val trianglePDF = TrianglePDF(geometryTriangle3SideShape, sheet)

        val pageInfo = PageInfo.Builder(A4X, A4Y, pageNumber).create()
        val page = this@pdfResult3SideTriangle.startPage(pageInfo)
        val canvas = page.canvas
        trianglePDF.ruler(AndroidCanvas(canvas))
        trianglePDF.ruler(AndroidCanvas(canvas))
        trianglePDF.drawTriangle(canvas)
        trianglePDF.sheetOnTriangle(canvas)
        this@pdfResult3SideTriangle.finishPage(page)
        val listOfSheet = trianglePDF.getLstOfSheet()
        Log.d("listOfSheet", listOfSheet.toString())
        if (single) {
            val otherParams = trianglePDF.getOtherParams()
            this@pdfResult3SideTriangle.addInfo(
                context,
                listOfSheet,
                fullRoof = false,
                pageNumber = pageNumber + 1,
                otherParams = otherParams,
            )
        }
        listOfSheet
    }

/**
 * Добавляет информацию относительно крыши целиком ,
 * либо фигуры (количество и длина листов, яндовая , покат и т.д)
 * [fullRoof] - если true, то [listOfSheet] умножается на 2, так считаться изначально будут
 * только разные стороны (для четырехскатки трапеция и треугольник по одному разу
 * ,а противоположные стороны им равны)
 */
suspend fun PdfDocument.addInfo(
    context: Context,
    listOfSheet: List<Sheet>,
    fullRoof: Boolean = false,
    otherParams: List<Pair<String, String>> = listOf(),
    paintText: TextPaint =
        TextPaint().apply {
            textSize = 20f
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        },
    pageNumber: Int,
) {
    withContext(Dispatchers.IO) {
        val pageInfo = PageInfo.Builder(A4X, A4Y, pageNumber).create()
        val page = this@addInfo.startPage(pageInfo)
        val canvas = page.canvas
        val x = A4X * 0.05f
        val startY = A4Y * 0.05f
        val textTrasfer = AddVerticalPaddingForLineText(startY)

        canvas.drawText("", x, textTrasfer.addTransferText(), paintText)
        val orderSheet = listOfSheet.groupingBy { it.ceilLen }.eachCount().toSortedMap()
        orderSheet.forEach { (k, v) ->
            canvas.drawText(
                "$k ${context.getString(R.string.cm)} = ${if (fullRoof) v * 2 else v}",
                x,
                textTrasfer.addTransferText(),
                paintText,
            )
        }
        repeat(2) { textTrasfer.addTransferText() }
        canvas.drawText(
            "${
                context.getString(
                    R.string.sheet_width,
                )
            } = ${listOfSheet.first().widthGeneral} -  ${listOfSheet.first().widthGeneral.value.toInt()} cm",
            x,
            textTrasfer.addTransferText(),
            paintText,
        )

        canvas.drawText(
            "${
                context.getString(
                    R.string.overlap,
                )
            } = ${listOfSheet.first().overlap} -  ${listOfSheet.first().overlap.value.toInt()} cm",
            x,
            textTrasfer.addTransferText(),
            paintText,
        )

        repeat(2) { textTrasfer.addTransferText() }

        otherParams.forEach { (k, v) ->
            canvas.drawText(
                "$k = $v",
                x,
                textTrasfer.addTransferText(),
                paintText,
            )
        }
        this@addInfo.finishPage(page)
    }
}

fun PdfDocument.renderContent(
    listOfPageContentBuilders: List<PageContentBuilder>,

    ) {
    listOfPageContentBuilders.forEachIndexed { index, pageContentBuilder ->
        createPage(pageContentBuilder = pageContentBuilder, pageNumber = index + 1)
    }
}

fun PdfDocument.createPage(
    pageContentBuilder: PageContentBuilder,
    pageNumber: Int

) {
    val pageInfo = pageContentBuilder.pageConfig.run { PageInfo.Builder(x, y, pageNumber).create() }
    val page = startPage(pageInfo)

    val canvas = page.canvas
    canvas.apply { pageContentBuilder.generateDraw(AndroidCanvas(this)) }
    finishPage(page)
}

data class PageContentBuilder(
    val pageConfig: PageConfig = PageConfig(),
    val generateDraw: CanvasInterface.() -> Unit,

    )

