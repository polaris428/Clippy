package com.polaris.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.model.ClipboardFolder
import com.polaris.shared.intent.MainIntent
import com.polaris.splash.intent.SplashIntent
import com.polaris.util.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(val postFolderUseCase: PostFolderUseCase) : ViewModel() {

    fun processIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.initPostFolder -> {
                initPostFolder(intent.ClipboardFolderList)
            }

        }
    }

    fun initPostFolder(list: List<ClipboardFolder>) = viewModelScope.launch {
        list.forEach {
            postFolderUseCase.execute(PrefManager.userSignInCheck, it, onComplete = {}).catch { }
        }

    }

}


