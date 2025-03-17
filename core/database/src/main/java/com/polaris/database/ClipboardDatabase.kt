package com.polaris.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polaris.database.model.ClipboardFolderEntity
import com.polaris.database.model.ClipboardItemEntity


@Database(entities = [ClipboardItemEntity::class], version = 1, exportSchema = false)
abstract class ClipboardDatabase : RoomDatabase() {
    abstract fun clipboardDao(): com.polaris.database.dao.ClipboardDao
    companion object {
        @Volatile
        private var INSTANCE: ClipboardDatabase? = null

        fun getDatabase(context: Context): ClipboardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClipboardDatabase::class.java,
                    "clipboard_database"
                ) .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Database(entities = [ClipboardFolderEntity::class], version = 1, exportSchema = false)
abstract class ClipboardFolderDatabase: RoomDatabase(){
    abstract fun clipboardFolderDao(): com.polaris.database.dao.ClipboardFolderDao

    companion object {
        @Volatile
        private var INSTANCE: ClipboardDatabase? = null

        fun getDatabase(context: Context): ClipboardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClipboardDatabase::class.java,
                    "clipboard_folders_database"
                ) .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}