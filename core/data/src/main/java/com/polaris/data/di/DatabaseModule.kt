package com.polaris.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.polaris.database.dao.ClipboardDao
import com.polaris.database.ClipboardDatabase
import com.polaris.database.dao.ClipboardFolderDao
import com.polaris.database.ClipboardFolderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideClipboardDatabase(@ApplicationContext context: Context): com.polaris.database.ClipboardDatabase {
        return Room.databaseBuilder(
            context,
            com.polaris.database.ClipboardDatabase::class.java,
            "clipboard_database"
        ).fallbackToDestructiveMigration()

            .fallbackToDestructiveMigration() //개발 테스트용 배포시 삭제 필요
            .build()
             .also { context.deleteDatabase("clipboard_database") } //개발 테스트용 배포시 삭제 필요
    }
    @Provides
    fun provideClipboardDao(database: com.polaris.database.ClipboardDatabase): com.polaris.database.dao.ClipboardDao {
        return database.clipboardDao()
    }



    @Provides
    @Singleton
    fun provideClipboardFolderDatabase(@ApplicationContext context: Context): com.polaris.database.ClipboardFolderDatabase {
        return Room.databaseBuilder(
            context,
            com.polaris.database.ClipboardFolderDatabase::class.java,
            "clipboard_folders_database"
        ).fallbackToDestructiveMigration()

            .fallbackToDestructiveMigration() //개발 테스트용 배포시 삭제 필요
            .build()
            .also { context.deleteDatabase("clipboard_folders_database") } //개발 테스트용 배포시 삭제 필요
    }

    @Provides
    fun provideClipboardFolderDao(database: com.polaris.database.ClipboardFolderDatabase): com.polaris.database.dao.ClipboardFolderDao {
        return database.clipboardFolderDao()
    }



    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}
