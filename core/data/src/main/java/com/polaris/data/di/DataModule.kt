package com.polaris.data.di

import com.polaris.data.repository.ClipboardRepository
import com.polaris.data.repository.ClipboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindClipboardRepository(
        clipboardRepositoryImpl: ClipboardRepositoryImpl
    ): ClipboardRepository
}