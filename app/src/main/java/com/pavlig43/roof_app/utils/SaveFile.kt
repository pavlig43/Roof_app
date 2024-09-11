package com.pavlig43.roof_app.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun saveFileUtils(context: Context, pdfFile: File?, saveNameFile: String){
    if (pdfFile !=null){
        val fileDPF = File(context.getExternalFilesDir(null),"$saveNameFile.pdf")
        FileOutputStream(fileDPF).use { outputStream->
            pdfFile!!.inputStream().use { inputStream->
                inputStream.copyTo(outputStream)
            }
        }


    }


}