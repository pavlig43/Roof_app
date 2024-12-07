package com.pavlig43.roofapp.di.files

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class SubDirModule {
    @Provides
    @SubDirName(SubDir.Roof)
    fun provideRoofSubDir(): SubDir = SubDir.Roof
}

@Qualifier
annotation class SubDirName(val subDirName: SubDir)
enum class SubDir(val title: String) {
    Roof("roof")
}
