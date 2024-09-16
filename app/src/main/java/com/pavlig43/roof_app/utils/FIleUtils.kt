package com.pavlig43.roof_app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH
import com.pavlig43.roof_app.BuildConfig
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.ui.shapes.quadrilateral.Geometry4SideShape
import com.pavlig43.roof_app.ui.shapes.quadrilateral.QuadroPDF
import com.pavlig43.roof_app.ui.shapes.triangle.GeometryTriangle3SideShape
import com.pavlig43.roof_app.ui.shapes.triangle.TrianglePDF
import java.io.File
import java.io.FileOutputStream

/**
 * Запускает окно, чтобы поделиться файлом в приложении на выбор
 */
fun sharePDFFile(context: Context, pdfFile: File){
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",
        pdfFile
    )
    val sharedIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM,uri)
    }
    context.startActivity(Intent.createChooser(sharedIntent, "Отправить"))

}

/**
 * Сохраняет файл с переданным именем в хранилище, файл можно посмотреть только из приложения, доступа к нему из других приложений нет
 */
fun saveFilePDF(context: Context, pdfFile: File?, saveNameFile: String){
    if (pdfFile !=null){
        val fileDPF = File(context.getExternalFilesDir(null),"$saveNameFile.pdf")
        FileOutputStream(fileDPF).use { outputStream->
            pdfFile!!.inputStream().use { inputStream->
                inputStream.copyTo(outputStream)
            }
        }


    }

}

/**
 * Проверяет , есть ли документ с переданным именем в списке документов в хранилище данного приложения для ПДФ
 */
fun checkSaveName(newName:String, context: Context,): Boolean {
    val directory = context.getExternalFilesDir(null)
    val listOfFiles =  directory?.listFiles()?.toList()?.map { it.name.split("/").last().replace(".pdf","") } ?: emptyList()
    return newName.isNotEmpty() && newName !in listOfFiles

}

fun PdfDocument.createFile(context: Context): File {
    val file = File(context.getExternalFilesDir(null), "roof.pdf")
    file.outputStream().use { this.writeTo(it) }
    this.close()
    return file
}

/**
 * В ПДФ документ вставляет рисунок 4х-угольника уже заполненный листами металла
 * и возвращает длины всех листов
 * [single] - если одиночная фигура , то добавляется лист с описанием этого 4х-угольника(размеры сторон, что_нибудь еще)
 */
fun PdfDocument.pdfResult4Side(
    geometry4SideShape: Geometry4SideShape,
    pageNumber:Int,
    single: Boolean = false,
    sheet: Sheet,

): MutableList<Sheet> {

    val quadroPDF = QuadroPDF(geometry4SideShape, sheet)

    val pageInfo = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, pageNumber).create()
    val page = this.startPage(pageInfo)
    val canvas = page.canvas

    quadroPDF.ruler(canvas)
    quadroPDF.ruler(canvas, horizontal = false, zero = true)
    quadroPDF.drawQuadro(canvas)
    quadroPDF.sheetOnQuadro(canvas)
    this.finishPage(page)
    val listOfSheet = quadroPDF.getLstOfSheet()
    if (single){
        val otherParams = quadroPDF.getOtherParams()
        this.addInfo(listOfSheet, fullRoof = false, pageNumber = pageNumber+1, otherParams = otherParams)
    }
    return listOfSheet

}
/**
 * В ПДФ документ вставляет рисунок 3х-угольника уже заполненный листами металла
 * и возвращает длины всех листов
 * [single] - если одиночная фигура , то добавляется лист с описанием этого триугольника(размеры сторон, что_нибудь еще)
 */
fun PdfDocument.pdfResult3SideTriangle(
    geometryTriangle3SideShape: GeometryTriangle3SideShape,
    pageNumber:Int,
    single:Boolean = false,
    sheet: Sheet,

    ): MutableList<Sheet> {

    val trianglePDF = TrianglePDF(geometryTriangle3SideShape, sheet)

    val pageInfo = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, pageNumber).create()
    val page = this.startPage(pageInfo)
    val canvas = page.canvas
    trianglePDF.ruler(canvas)
    trianglePDF.ruler(canvas, horizontal = false, zero = true)
    trianglePDF.drawTriangle(canvas)
    trianglePDF.sheetOnTriangle(canvas)
    this.finishPage(page)
    val listOfSheet = trianglePDF.getLstOfSheet()
    if (single){
        val otherParams = trianglePDF.getOtherParams()
        this.addInfo(listOfSheet, fullRoof = false, pageNumber = pageNumber+1, otherParams = otherParams)
    }
    return listOfSheet
}

/**
 * Добавляет информацию относительно крыши целиком ,либо фигуры (количество и длина листов, яндовая , покат и т.д)
 * [fullRoof] - если true, то [listOfSheet] умножается на 2, так считаться изначально будут только разные стороны (для четырехскатки трапеция и треугольник по одному разу
 * ,а противоположные стороны им равны)
 */
fun PdfDocument.addInfo(
    listOfSheet: List<Sheet>,
    fullRoof:Boolean = false,
    otherParams:List<Pair<String,String>> = listOf(),
     paintText: TextPaint = TextPaint().apply {
        textSize = 20f
        flags = flags or Paint.UNDERLINE_TEXT_FLAG
    },
    pageNumber:Int,
    ){
    val pageInfo = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, pageNumber).create()
    val page = this.startPage(pageInfo)
    val canvas = page.canvas
    val x = A4WIDTH * 0.05f
    val startY = A4HEIGHT *0.05f
    val textTrasfer = AddVerticalPaddingForLineText(startY)

    canvas.drawText("Все листы",x,textTrasfer.addTransferText(),paintText)
    val orderSheet = listOfSheet.groupingBy { it.ceilLen }.eachCount().toSortedMap()
    orderSheet.forEach { (k, v) ->
        canvas.drawText(
            "$k cm = ${if (fullRoof) v*2 else v}",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
    }
    repeat(2){ textTrasfer.addTransferText()}
    canvas.drawText("Общая ширина листа -  ${listOfSheet.first().widthGeneral.toInt()} cm",x,textTrasfer.addTransferText(),paintText)
    canvas.drawText("Рабочая ширина листа -  ${listOfSheet.first().visible.toInt()} cm",x,textTrasfer.addTransferText(),paintText)
    canvas.drawText("Перехлест -  ${listOfSheet.first().overlap.toInt()} cm",x,textTrasfer.addTransferText(),paintText)

    repeat(2){ textTrasfer.addTransferText()}

    otherParams.forEach {
            (k, v) ->
        canvas.drawText(
            "$k = $v",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
    }
    this.finishPage(page)

}