package com.polaris.clipboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard.intent.ClipboardIntent
import com.polaris.clipboard.state.ClipboardUiState
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.isUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.polaris.domin.usecase.clipboard.clipboard.PostClipboardInsertUseCase
import com.polaris.model.model.ClipboardItem
import com.polaris.util.PrefManager
import com.polaris.util.extractUrl
import com.polaris.util.getWebTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClipboardViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<ClipboardUiState>(ClipboardUiState.Initialize)
    val uiState: StateFlow<ClipboardUiState> = _uiState

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem : StateFlow<ClipboardItem> = _clipboardItem


    private val _sharedText = MutableStateFlow<String>("")
    val sharedText : StateFlow<String> = _sharedText

    fun processIntent(intent: ClipboardIntent) {
        when (intent) {


            is ClipboardIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.txext)
            }
        }
    }

    fun siteInformation(url: String): Job = viewModelScope.launch {
        val clipboardItem = if (isUrl(url)) {
            val urlPreprocessing = extractUrl(url)

            // 비동기 처리 보장
            val type = withContext(Dispatchers.IO) {
                getWebTitle(urlPreprocessing)
            }
            val title = withContext(Dispatchers.IO){
                fetchWebTitle(urlPreprocessing)
            }
            Log.e("polaris0428",type)
            Log.e("polaris0428",title)
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

    fun postClipboardInsert(id: String): Job = viewModelScope.launch(Dispatchers.IO) {



        postClipboardInsertUseCase.execute(
            folderId = id,
            item = clipboardItem.value,
           ).collect {

        }

    }


}
