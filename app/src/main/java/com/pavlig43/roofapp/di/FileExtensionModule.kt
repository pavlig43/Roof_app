package com.pavlig43.roofapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class FileExtensionModule {

    @Provides
    @Extension(FileExtension.TXT)
    fun provideTXTExtension(): FileExtension = FileExtension.TXT

    @Provides
    @Extension(FileExtension.DOC)
    fun provideDOCExtension(): FileExtension = FileExtension.DOC

    @Provides
    @Extension(FileExtension.PDF)
    fun providePDFExtension(): FileExtension = FileExtension.PDF

    @Provides
    @Extension(FileExtension.JSON)
    fun provideJSONExtension(): FileExtension = FileExtension.JSON

    @Provides
    @Extension(FileExtension.CSV)
    fun provideCSVExtension(): FileExtension = FileExtension.CSV
}

@Qualifier
annotation class Extension(val fileExtension: FileExtension)

enum class FileExtension(val value: String) {
    TXT("txt"),
    DOC("doc"),
    PDF("pdf"),
    JSON("json"),
    CSV("csv");

    val defaultName: String
        get() = "default.$value"
}
