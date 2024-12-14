package com.pavlig43.roofapp.data.fileStorage

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.FileProvider
import com.pavlig43.roof_app.BuildConfig
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.di.files.FileExtension
import com.pavlig43.roofapp.di.files.SubDir
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

/**
 * Класс AndroidFileStorageRepository — это реализация интерфейса FileStorageRepository,
 * предназначенная для управления файлами в Android-приложении. Он использует [context] для
 * взаимодействия с файловой системой Android, поддерживает асинхронные операции с помощью
 * [dispatcher] и работает с файлами определённого формата, заданного через параметр [fileExtension].
 * Свойство [_listOfFiles] представляет собой SnapshotStateList<File>, который инициализируется
 * результатом метода [getListOfFile].
 * Метод [createFile] создаёт новый файл в директории документов.
 * Свойство [listOfFiles] предоставляет Flow, обновляющийся при изменении списка [_listOfFiles]
 *  Метод [shareFile] позволяет делиться файлами через Android Intent.
 * Метод [saveAndGetFilePath] создаёт файл и получает его имя через переданную
 * suspend лямбда-функцию.
 * Метод [delete] удаляет существующий файл,если он не является файлом по умолчанию.
 * Метод [saveFileWithNewName] сохраняет содежимое исходного файла в новый файл с заданным именем.
 * и обновляет список файлов.
 * Метод [loadFile] возвращает файл как Flow или выбрасывает FileNotFoundException, если файл не найден.
 * Метод [checkSaveName] проверяет, доступно ли имя для сохранения, гарантируя, что имя уникально и не пусто.
 * Метод [getListOfFile] получает все файлы с указанным расширением, исключая файл по умолчанию,
 * из директории документов.

 */
class AndroidFileStorageRepository(
    private val context: Context,
    override val fileExtension: FileExtension,
    private val dispatcher: CoroutineDispatcher,
    override val subDir: SubDir

) :
    FileStorageRepository {
    private val folderFile by lazy {
        val folder = File(Environment.DIRECTORY_DOCUMENTS, subDir.title)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        context.getExternalFilesDir(folder.toString())
    }

    private val _listOfFiles: SnapshotStateList<File> =
        mutableStateListOf<File>().apply { addAll(getListOfFile()) }

    override suspend fun createFile(shortFileName: String): File {
        val file =
            File(folderFile, shortFileName)
        return file
    }

    override val listOfFiles: Flow<List<File>> =
        snapshotFlow { _listOfFiles.toList() }

    override fun shareFile(file: File, typeValue: String) {
        val uri =
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file,
            )
        val sharedIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                type = typeValue
                putExtra(Intent.EXTRA_STREAM, uri)
            }

        context.startActivity(
            Intent.createChooser(
                sharedIntent,
                context.getString(R.string.send)
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override suspend fun saveAndGetFilePath(

        createAndGetFilePath: suspend (CoroutineDispatcher, File) -> String
    ): String {
        val file = createFile()
        return createAndGetFilePath(dispatcher, file)
    }

    override suspend fun delete(file: File) {
        if (file.exists() && file.name != fileExtension.defaultName) {
            file.delete()
        }
        _listOfFiles.remove(file)
    }

    override suspend fun saveFileWithNewName(file: File, fileName: String) {
        val saveFile = createFile("$fileName.${fileExtension.value}")
        withContext(dispatcher) {
            file.copyTo(saveFile, overwrite = true)
            _listOfFiles.add(saveFile)
        }
    }

    override fun loadFile(filePath: String): Flow<File> = flow {
        val file = File(filePath)
        if (!file.exists()) {
            throw FileNotFoundException("File with name $filePath not found")
        }
        emit(file)
    }

    override fun checkSaveName(newName: String): Flow<Boolean> = flow {
        val allFiles = folderFile?.listFiles()
            ?: run {
                emit(false)
                return@flow
            }
        val check: Boolean =
            "$newName.${fileExtension.value}" !in allFiles.map { it.name } && newName.isNotBlank()
        emit(check)
    }

    private fun getListOfFile(): List<File> {
        val listOfAllFiles =
            folderFile?.listFiles()
                ?: return emptyList()
        return listOfAllFiles.filter { it.extension == fileExtension.value }
            .filter { it.name != fileExtension.defaultName }
    }
}
