package com.pavlig43.roofapp.di

import android.content.Context
import com.pavlig43.roofapp.data.fileStorage.AndroidFileStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAndroidFileStorageRepository(
        @ApplicationContext context: Context,
        @Extension(FileExtension.PDF) extension: FileExtension,
        @Dispatcher(DispatcherType.IO) dispatcher: CoroutineDispatcher
    ): AndroidFileStorageRepository {
        return AndroidFileStorageRepository(context, extension, dispatcher)
    }
}
