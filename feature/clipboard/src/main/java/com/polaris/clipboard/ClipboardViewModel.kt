package com.polaris.clipboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard.state.ClipboardState

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
class ClipboardViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase,
    private val getLocalClipboardFolderNameUseCase: GetLocalClipboardFolderNameUseCase,


    ) : ViewModel() {

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem



    private val _folderNameList = MutableStateFlow<List<FolderInfo>>(listOf())
    val folderNameList: StateFlow<List<FolderInfo>> = _folderNameList

    private val _intent = MutableSharedFlow<ClipboardIntent>()
    private val intent: SharedFlow<ClipboardIntent> = _intent.asSharedFlow()

    private val _uiState = MutableStateFlow<ClipboardState>(ClipboardState.Initialize)
    val uiState: StateFlow<ClipboardState> =_uiState

    init {
        handleIntent()
    }



    fun sendIntent(intent: ClipboardIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
    fun updateUiState(clipboardSaveUiState: ClipboardState) {
        viewModelScope.launch {
            _uiState.emit(clipboardSaveUiState)
        }
    }
    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is ClipboardIntent.getUrlCrawlingInfo -> {
                        getUrlCrawlingInfo(intent.url)
                    }

                    is ClipboardIntent.getLocalClipboardFolderName ->{
                        getLocalClipboardFolderName()
                    }

                    is ClipboardIntent.postClipboarInsertIntent -> {
                        postClipboardInsert(clipboardItem = clipboardItem.value , id = intent.folderInfo.id)
                    }
                }
            }
        }
    }


    fun getLocalClipboardFolderName(): Job = viewModelScope.launch(Dispatchers.IO) {
        getLocalClipboardFolderNameUseCase.execute().collect{
            _folderNameList.value = it
            updateUiState(ClipboardState.SiteCrawlingStart)
        }
    }

    fun getUrlCrawlingInfo(url: String): Job = viewModelScope.launch {

        val clipboardItem = if (isUrl(url)) {
            val urlPreprocessing = extractUrl(url)

            // 비동기 처리 보장
            val type = withContext(Dispatchers.IO) {
                getWebTitle(urlPreprocessing)
            }
            val title = withContext(Dispatchers.IO) {
                fetchWebTitle(urlPreprocessing)
            }

            ClipboardItem(
                type = type,
                url = urlPreprocessing,
                title = title ?: "",
                faviconUrl = getGoogleFaviconUrl(urlPreprocessing)
            )
        } else {
            ClipboardItem(
                type = "text",
                url = null,
                title = url,
                faviconUrl = null
            )
        }
        updateUiState(ClipboardState.SiteCrawlingComplete)
        _clipboardItem.emit(clipboardItem) // value 대신 emit 사용
    }

    fun postClipboardInsert(id: String, clipboardItem: ClipboardItem): Job =
        viewModelScope.launch(Dispatchers.IO) {


            postClipboardInsertUseCase.execute(
                folderId = id,
                item = clipboardItem,
            ).collect {
                updateUiState(ClipboardState.ClipboardSaveSuccess)
            }

        }






}