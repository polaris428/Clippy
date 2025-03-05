package com.polaris.clippy

import android.app.Application
import com.polaris.util.PrefManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClipboardApplication : Application() {
    companion object {

        @Volatile
        lateinit var instance: ClipboardApplication


    }

    init {
        instance = this


    }

    override fun onCreate() {
        super.onCreate()
        PrefManager.init(applicationContext)

    }
}