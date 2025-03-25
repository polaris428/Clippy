package com.polaris.main

import com.polaris.splash.intent.SplashIntent


sealed class MainIntent {
    data class getAllClipboardListIntent(val folderIdList:List<String>): MainIntent()



}