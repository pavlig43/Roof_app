package com.pavlig43.roof_app.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.pavlig43.roof_app.BuildConfig
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