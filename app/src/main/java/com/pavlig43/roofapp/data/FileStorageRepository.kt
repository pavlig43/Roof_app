package com.pavlig43.roofapp.data

import com.pavlig43.roofapp.di.FileExtension
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Интерфейс FileStorageRepository предоставляет функционал для управления файлами в системе
 * хранения. Он включает операции для создания, переименования, удаления, обмена и получения файлов,
 * а также проверки имени файла и получения списков файлов. Свойство [fileExtension] представляет
 * формат файлов и настройки их именования по умолчанию. Метод [createFile] создаёт новый файл с
 * необязательным параметром имени, которое по умолчанию берётся из fileExtension.defaultName.
 * Свойство [listOfFiles] представляет собой  Flow, который возвращает список файлов,
 * доступных в хранилище. Функция [shareFile] позволяет поделиться указанным файлом с заданным
 * MIME-типом, по умолчанию — "application/pdf". Метод [saveAndGetFileName] сохраняет файл и
 * возвращает его имя, используя при этом suspend лямбда-функцию, которая принимает
 * CoroutineDispatcher и File. Метод [delete] удаляет указанный файл из хранилища, а [reNameFile]
 * переименовывает файл в новое имя. Функция [loadFile] загружает файл в виде Flow.
 * Метод [checkSaveName] проверяет, можно ли использовать заданное имя для сохранения
 * файла, возвращая Flow<Boolean>.
 */
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
}
