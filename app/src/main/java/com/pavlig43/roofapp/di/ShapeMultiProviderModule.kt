package com.pavlig43.roofapp.di

import com.pavlig43.roofapp.data.shapeMulti.ShapeMultiProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ShapeMultiProviderModule {

    @Provides
    @Singleton
    fun provideShapeMultiProvider(): ShapeMultiProvider = ShapeMultiProvider()
}
