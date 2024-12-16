package com.pavlig43.roofapp.di

import androidx.lifecycle.SavedStateHandle
import com.pavlig43.pdfBox.PdfBoxDrawText
import com.pavlig43.roofapp.FILE_NAME
import com.pavlig43.roofapp.MARK_INFO
import com.pavlig43.roofapp.data.resourceProvider.AndroidResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
class PdfBoxDrawTextModule {
    @Provides
    fun providePdfBoxDrawText(
        saveStateHandle: SavedStateHandle,
        @Dispatcher(DispatcherType.IO) dispatcher: CoroutineDispatcher,
        resourceProvider: AndroidResourceProvider
    ): PdfBoxDrawText {
        return PdfBoxDrawText(
            filePath = checkNotNull(saveStateHandle[FILE_NAME]),
            dispatcher = dispatcher,
            fontInputStream = resourceProvider.getFontInputStream("font/Roboto-Medium.ttf"),
            markInfo = MARK_INFO
        )
    }
}
