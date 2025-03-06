package com.polaris.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.polaris.data.local.ClipboardDao
import com.polaris.data.local.ClipboardDatabase
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
    fun provideClipboardDatabase(@ApplicationContext context: Context): ClipboardDatabase {
        return Room.databaseBuilder(
            context,
            ClipboardDatabase::class.java,
            "clipboard_database"
        ).fallbackToDestructiveMigration()

            .fallbackToDestructiveMigration() //개발 테스트용 배포시 삭제 필요
            .build()
             //.also { context.deleteDatabase("clipboard_database") } //개발 테스트용 배포시 삭제 필요
    }

    @Provides
    fun provideClipboardDao(database: ClipboardDatabase): ClipboardDao {
        return database.clipboardDao()
    }
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}
