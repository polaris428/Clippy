package com.polaris.data.di

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.data.repository.LocalClipboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindClipboardRepository(
        clipboardRepositoryImpl: LocalClipboardRepositoryImpl
    ): com.polaris.domin.repository.LocalClipboardRepository
}