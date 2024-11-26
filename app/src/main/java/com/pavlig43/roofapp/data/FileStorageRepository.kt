package com.pavlig43.roofapp.data

import com.pavlig43.roofapp.di.FileExtension
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileStorageRepository {
    val fileExtension: FileExtension
    suspend fun createFile(shortFileName: String = fileExtension.defaultName): File
    val listOfFiles: Flow<List<File>>
    fun shareFile(file: File, typeValue: String = "application/pdf")
    suspend fun saveAndGetFileName(
        createAndGetFileName: suspend (CoroutineDispatcher, File) -> String
    ): String

    suspend fun delete(file: File)
    suspend fun reNameFile(file: File, fileName: String)
    fun loadFile(fileName: String): Flow<File>
    fun checkSaveName(newName: String): Flow<Boolean>
    fun getListOfFile(): List<File>
}
