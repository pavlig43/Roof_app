package com.pavlig43.roof_app.utils

import android.content.Context
import kotlinx.coroutines.flow.update

fun checkSaveName(newName:String, context: Context,): Boolean {
    val directory = context.getExternalFilesDir(null)
    val listOfFiles =  directory?.listFiles()?.toList()?.map { it.name.split("/").last().replace(".pdf","") } ?: emptyList()
    return newName.isNotEmpty() && newName !in listOfFiles


}