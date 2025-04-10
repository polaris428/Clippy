package com.polaris.main_save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.local.folder.GetLocalClipboardFolderNameUseCase

import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardInsertUseCase
import com.polaris.main_save.intent.ClipboardSaveIntent
import com.polaris.main_save.state.ClipboardSaveState
import com.polaris.model.model.ClipboardItem
import com.polaris.util.extractUrl
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.getWebTitle
import com.polaris.util.isUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ClipboardSaveViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<ClipboardSaveState>(ClipboardSaveState.Initialize)
    val uiState: StateFlow<ClipboardSaveState> get() = _uiState

    private val _intent = MutableSharedFlow<ClipboardSaveIntent>()
    val intent: SharedFlow<ClipboardSaveIntent> = _intent.asSharedFlow()

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem

    private val _folderNameList = MutableStateFlow<List<String>>(listOf())
    val folderNameList:StateFlow <List<String>> = _folderNameList
    init {
        handleIntent()
    }

    fun updateUiState(clipboardSaveUiState: ClipboardSaveState) {
        viewModelScope.launch {
            _uiState.emit(clipboardSaveUiState)
        }
    }

    fun sendIntent(intent: ClipboardSaveIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {


                    is ClipboardSaveIntent.postClipboarInsertIntent -> {
                        updateUiState(ClipboardSaveState.ClipboardSaveLoading)
                        postClipboardInsert(intent.folderId, intent.clipboardItem)
                    }


                }
            }
        }
    }

    fun postClipboardInsert(id: String, clipboardItem: ClipboardItem): Job =
        viewModelScope.launch(Dispatchers.IO) {


            postClipboardInsertUseCase.execute(
                folderId = id,
                item = clipboardItem,
            ).collect {
                updateUiState(ClipboardSaveState.ClipboardSaveSuccess)
            }

        }





    fun updateClipboardItem(clipboardItem: ClipboardItem){

        _clipboardItem.value = clipboardItem
    }




}


