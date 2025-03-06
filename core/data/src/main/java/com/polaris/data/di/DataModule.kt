package com.polaris.data.di

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.data.repository.LocalClipboardRepositoryImpl
import com.polaris.data.repository.RemoteClipboardRepositoryImpl
import com.polaris.domin.repository.RemoteClipboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindLocalClipboardRepository(
        clipboardRepositoryImpl: LocalClipboardRepositoryImpl
    ): LocalClipboardRepository

    @Binds
    internal abstract fun bindRemoteClipboardRepository(
        remoteClipboardRepositoryImpl: RemoteClipboardRepositoryImpl
    ): RemoteClipboardRepository
}