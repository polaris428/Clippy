package com.polaris.clipboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard.intent.ClipboardSaveIntent
import com.polaris.clipboard.state.ClipboardSaveState

import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardInsertUseCase
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

                    is ClipboardSaveIntent.getUrlCrawlingInfo -> {
                        updateUiState(ClipboardSaveState.ClipboardCrawlingInfo)
                        getUrlCrawlingInfo(intent.url)
                    }

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

        _clipboardItem.emit(clipboardItem) // value 대신 emit 사용
    }

}


