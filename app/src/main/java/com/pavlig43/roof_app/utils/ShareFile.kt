package com.pavlig43.roof_app.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.pavlig43.roof_app.BuildConfig
import java.io.File

fun sharePDFFile(context: Context,pdfFile:File){
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