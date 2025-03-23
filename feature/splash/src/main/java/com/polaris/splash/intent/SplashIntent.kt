package com.polaris.splash.intent


sealed class SplashIntent {
    data class getAllClipboardListIntent(val folderIdList:List<String>): SplashIntent()
    object  getLocalAllClipboardListIntent: SplashIntent()
    object  postLocalFolderSyncUseCase: SplashIntent()
}