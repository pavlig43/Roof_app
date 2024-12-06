package com.pavlig43.roofapp.di

import android.content.Context
import com.pavlig43.roofapp.data.fileStorage.AndroidFileStorageRepository
import com.pavlig43.roofapp.di.files.Extension
import com.pavlig43.roofapp.di.files.FileExtension
import com.pavlig43.roofapp.di.files.SubDir
import com.pavlig43.roofapp.di.files.SubDirName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FileStorageModule {
    @Provides
    @Singleton
    fun provideRoofAndroidFileStorageRepositoryPdf(
        @ApplicationContext context: Context,
        @Extension(FileExtension.PDF) extension: FileExtension,
        @Dispatcher(DispatcherType.IO) dispatcher: CoroutineDispatcher,
        @SubDirName(SubDir.Roof) subDir: SubDir
    ): AndroidFileStorageRepository {
        return AndroidFileStorageRepository(context, extension, dispatcher, subDir)
    }
}
