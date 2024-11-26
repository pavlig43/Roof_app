package com.pavlig43.roofapp.data

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.FileProvider
import com.pavlig43.roof_app.BuildConfig
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.di.FileExtension
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class AndroidFileStorageRepository @Inject constructor(
    private val context: Context,
    override val fileExtension: FileExtension,
    private val dispatcher: CoroutineDispatcher

) :
    FileStorageRepository {
    private val _listOfFiles: SnapshotStateList<File> =
        mutableStateListOf<File>().apply { addAll(getListOfFile()) }

    override suspend fun createFile(shortFileName: String): File {
        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), shortFileName)

        Log.d("createFile", file.absolutePath)
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

    override suspend fun saveAndGetFileName(

        createAndGetFileName: suspend (CoroutineDispatcher, File) -> String
    ): String {
        val file = createFile()
        return createAndGetFileName(dispatcher, file)
    }

    override suspend fun delete(file: File) {
        if (file.exists() && file.name != fileExtension.defaultName) {
            file.delete()
        }
        _listOfFiles.remove(file)
    }

    override suspend fun reNameFile(file: File, fileName: String) {
        val saveFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "$fileName.${fileExtension.value}"
        )
        withContext(dispatcher) {
            if (file.renameTo(saveFile)) {
                _listOfFiles.add(saveFile)
            }
        }
    }

    override fun loadFile(fileName: String): Flow<File> = flow {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: throw FileNotFoundException("Directory not found")
        val file = File(directory, fileName)
        if (!file.exists()) {
            throw FileNotFoundException("File with name $fileName not found")
        }
        emit(file)
    }

    override fun checkSaveName(newName: String): Flow<Boolean> = flow {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val allFiles = directory?.listFiles()
            ?: run {
                emit(false)
                return@flow
            }
        val check: Boolean =
            "$newName.${fileExtension.value}" !in allFiles.map { it.name } && newName.isNotBlank()
        emit(check)
    }

    override fun getListOfFile(): List<File> {
        val listOfAllFiles =
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.listFiles()
                ?: return emptyList()
        return listOfAllFiles.filter { it.extension == fileExtension.value }
            .filter { it.name != fileExtension.defaultName }
    }
}
