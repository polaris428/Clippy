package com.polaris.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.local.folder.GetLocalClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.remote.folder.PostFolderSyncUseCase
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem

import com.polaris.splash.intent.SplashIntent
import com.polaris.splash.state.SplashState
import com.polaris.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(

    private val getClipboardAllFolderUseCase: GetClipboardAllUseCase,
    private val getLocalClipboardAllUseCase: GetLocalClipboardAllUseCase,
    private val postFolderSyncUseCase: PostFolderSyncUseCase,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashState>(SplashState.Initialize)
    val uiState: StateFlow<SplashState> get() = _uiState

    private val _intent = MutableSharedFlow<SplashIntent>()
    val intent: SharedFlow<SplashIntent> = _intent.asSharedFlow()
    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem

    private val _clipboardDataList = MutableStateFlow<List<ClipboardFolder>>(listOf())
    val clipboardDataList: StateFlow<List<ClipboardFolder>> = _clipboardDataList

    private val _localClipboardDataList = MutableStateFlow<List<ClipboardFolder>>(listOf())
    val localClipboardDataList: StateFlow<List<ClipboardFolder>> = _localClipboardDataList

    private val _remoteApiComplete = MutableStateFlow<Boolean>(false)
    val remoteApiComplete: StateFlow<Boolean> = _remoteApiComplete

    private val _localApiComplete = MutableStateFlow<Boolean>(false)
    val localApiComplete: StateFlow<Boolean> = _remoteApiComplete

    init {
        handleIntent()
    }

    fun updateUiState(signInState: SplashState) {
        viewModelScope.launch {
            _uiState.emit(signInState)
        }
    }

    fun sendIntent(intent: SplashIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is SplashIntent.getAllClipboardListIntent -> {
                        updateUiState(SplashState.Loading)
                        getClipboardFolder(intent.folderIdList)
                    }
                    is SplashIntent.getLocalAllClipboardListIntent -> {
                        updateUiState(SplashState.Loading)
                        getLocalClipboardFolder()
                    }
                    is SplashIntent.postLocalFolderSyncUseCase -> {
                        postFolderSync(oldFolders =localClipboardDataList.value, newFolders =clipboardDataList.value)

                    }
                }
            }
        }
    }


    fun getClipboardFolder(folderId: List<String>): Job = viewModelScope.launch {

        getClipboardAllFolderUseCase.execute(folderId).collect {
            _clipboardDataList.value = it
            _remoteApiComplete.value =true
            if (remoteApiComplete.value && localApiComplete.value){
                updateUiState(SplashState.ApiSuccess)
            }

        }


    }

    fun getLocalClipboardFolder(): Job = viewModelScope.launch(Dispatchers.IO) {

        getLocalClipboardAllUseCase.execute().collect {
            _localClipboardDataList.value = it
            _localApiComplete.value =true
            if (remoteApiComplete.value && localApiComplete.value){
                updateUiState(SplashState.ApiSuccess)
            }

        }


    }
    fun postFolderSync(
        oldFolders: List<ClipboardFolder>,
        newFolders: List<ClipboardFolder>
    ) = viewModelScope.launch {
        postFolderSyncUseCase.execute(oldFolders = oldFolders, newFolders = newFolders).collect {
            if (it){
                updateUiState(SplashState.Complete)
            }else{
                updateUiState(SplashState.Error("동기화 실패"))
            }
        }
    }




}


