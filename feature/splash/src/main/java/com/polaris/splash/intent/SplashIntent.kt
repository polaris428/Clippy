package com.polaris.splash.intent

import com.polaris.model.model.ClipboardFolder


sealed class SplashIntent {
    data class initPostFolder(val ClipboardFolderList: List<ClipboardFolder>) : SplashIntent()
}