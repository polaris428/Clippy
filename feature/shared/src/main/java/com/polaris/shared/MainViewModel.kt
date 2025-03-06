package com.polaris.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import com.polaris.domin.usecase.clipboard.UpdateClipboardUseCase
import com.polaris.model.ClipboardItem
import com.polaris.shared.intent.MainIntent
import com.polaris.util.extractUrl
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.getWebTitle
import com.polaris.util.isUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getClipboardAllUseCase: GetClipboardAllUseCase,
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase,
    private val updateClipboardUseCase: UpdateClipboardUseCase,
) : ViewModel() {


    private val _clipboardItem = MutableStateFlow<ClipboardItem>(com.polaris.model.ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem

    private val _clipboardDataList = MutableStateFlow<List<com.polaris.model.ClipboardItem>>(emptyList())
    val clipboardDataList: StateFlow<List<com.polaris.model.ClipboardItem>> = _clipboardDataList

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.getAllClipboardListIntent -> {
                getAllClipboardList()
            }

            is MainIntent.postClipboarInsertIntent -> {
                postClipboardInsert()
            }

            is MainIntent.updateClipboarIntent -> {
                updateClipboard()
            }


        }
    }

    fun getAllClipboardList(): Job = viewModelScope.launch {

        getClipboardAllUseCase.execute(onComplete = {}).collect {
            _clipboardDataList.value = it

        }

    }

    fun postClipboardInsert(): Job = viewModelScope.launch(Dispatchers.IO) {


        postClipboardInsertUseCase.execute(
            item = clipboardItem.value,
            onComplete = {

            }).collect {

        }

    }

    fun siteInformation(url: String): Job = viewModelScope.launch {
        val clipboardItem = if (isUrl(url)) {
            val urlPreprocessing = extractUrl(url)

            // 비동기 처리 보장
            val type = withContext(Dispatchers.IO) {
                getWebTitle(urlPreprocessing)
            }
            val title = withContext(Dispatchers.IO) {
                fetchWebTitle(urlPreprocessing)
            }

            com.polaris.model.ClipboardItem(
                type = type,
                url = urlPreprocessing,
                title = title ?: "",
                faviconUrl = getGoogleFaviconUrl(urlPreprocessing)
            )
        } else {
            com.polaris.model.ClipboardItem(
                type = "text",
                url = null,
                title = url,
                faviconUrl = null
            )
        }

        _clipboardItem.emit(clipboardItem) // value 대신 emit 사용
    }

    fun updateClipboardItem(item: com.polaris.model.ClipboardItem) {
        _clipboardItem.value = item
    }

    fun updateClipboardItem(type: String, title: String) {
        _clipboardItem.value.type = type
        _clipboardItem.value.title = title
        _clipboardItem.value.timestamp = System.currentTimeMillis()


    }


    fun updateClipboard(): Job = viewModelScope.launch {
        updateClipboardUseCase.execute(clipboardItem = clipboardItem.value, onComplete = {})
            .collect {

                processIntent(MainIntent.getAllClipboardListIntent)
            }


    }
}
