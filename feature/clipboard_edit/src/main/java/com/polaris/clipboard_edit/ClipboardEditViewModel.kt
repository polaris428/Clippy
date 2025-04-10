package com.polaris.clipboard_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard_edit.intent.ClipboardEditIntent
import com.polaris.clipboard_edit.state.ClipboardEditState
import com.polaris.domin.usecase.clipboard.local.folder.GetLocalClipboardFolderNameUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardInsertUseCase
import com.polaris.model.model.ClipboardItem
import com.polaris.model.model.FolderInfo
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
class ClipboardEditViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase,
    private val getLocalClipboardFolderNameUseCase: GetLocalClipboardFolderNameUseCase,


    ) : ViewModel() {

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem



    private val _folderNameList = MutableStateFlow<List<FolderInfo>>(listOf())
    val folderNameList: StateFlow<List<FolderInfo>> = _folderNameList

    private val _intent = MutableSharedFlow<ClipboardEditIntent>()
    private val intent: SharedFlow<ClipboardEditIntent> = _intent.asSharedFlow()

    private val _uiState = MutableStateFlow<ClipboardEditState>(ClipboardEditState.Initialize)
    val uiState: StateFlow<ClipboardEditState> =_uiState
    var isInitialized = false
    init {
        handleIntent()
    }



    fun sendIntent(intent: ClipboardEditIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
    fun updateUiState(clipboardSaveUiState: ClipboardEditState) {
        viewModelScope.launch {
            _uiState.emit(clipboardSaveUiState)
        }
    }
    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {

                    is ClipboardEditIntent.postClipboarInsertIntent -> {

                        postClipboardInsert(clipboardItem = clipboardItem.value , id = intent.folderId)
                    }
                    is ClipboardEditIntent.getLocalClipboardFolderName ->{
                        getLocalClipboardFolderName()
                    }
                }
            }
        }
    }



    fun getLocalClipboardFolderName(): Job = viewModelScope.launch(Dispatchers.IO) {
        getLocalClipboardFolderNameUseCase.execute().collect{
            _folderNameList.value = it

        }
    }


    fun postClipboardInsert(id: String, clipboardItem: ClipboardItem): Job =
        viewModelScope.launch(Dispatchers.IO) {


            postClipboardInsertUseCase.execute(
                folderId = id,
                item = clipboardItem,
            ).collect {
                updateUiState(ClipboardEditState.ClipboardSaveSuccess)
            }

        }


    fun updateClipboardItem(type: String, title: String) {
        _clipboardItem.value.type = type
        _clipboardItem.value.title = title
        _clipboardItem.value.timestamp = System.currentTimeMillis()


    }
    fun updateClipboardItem(clipboardItem: ClipboardItem) {
        _clipboardItem.value = clipboardItem


    }






}