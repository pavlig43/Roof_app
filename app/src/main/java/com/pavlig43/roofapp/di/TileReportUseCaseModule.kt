package com.pavlig43.roofapp.di

import com.pavlig43.roofapp.data.docBuilder.AndroidPdfBuilder
import com.pavlig43.roofapp.data.resourceProvider.AndroidResourceProvider
import com.pavlig43.roofapp.domain.useCase.TileReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
class TileReportUseCaseModule {

    @Provides
    @DocTypeBuilder(DocType.AndroidPdf)
    fun provideAndroidPdfUseCase(
        docBuilder: AndroidPdfBuilder,
        resourceProvider: AndroidResourceProvider
    ): TileReportUseCase {
        return TileReportUseCase(docBuilder, resourceProvider)
    }
}

@Qualifier
annotation class DocTypeBuilder(val type: DocType)
enum class DocType {
    AndroidPdf
}
