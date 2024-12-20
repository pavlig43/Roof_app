package com.pavlig43.roofapp.di

import android.content.Context
import com.pavlig43.roofapp.data.resourceProvider.AndroidResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ResourceProviderModule {
    @Provides
    @Singleton
    fun provideAndroidResourceProvider(@ApplicationContext context: Context) =
        AndroidResourceProvider(context)
}
