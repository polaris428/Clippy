package com.polaris.splash.intent

import com.polaris.model.model.ClipboardFolder
import com.polaris.shared.intent.MainIntent


sealed class SplashIntent {
    data class getAllClipboardListIntent(val folderIdList:List<String>): SplashIntent()
    object  getLocalAllClipboardListIntent: SplashIntent()
    object  postLocalFolderSyncUseCase: SplashIntent()
}