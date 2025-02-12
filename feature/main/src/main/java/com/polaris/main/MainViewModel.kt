package com.polaris.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.isUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val getClipboardAllUseCase: GetClipboardAllUseCase,
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.ClipboardList)
    val uiState: StateFlow<MainUiState> = _uiState

    private val _clipboardDataList = MutableStateFlow<List<ClipboardItem>>(emptyList())
    val clipboardDataList :StateFlow<List<ClipboardItem>> =  _clipboardDataList

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.getAllClipboardListIntent -> {
                getAllClipboardList()
            }

            is MainIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.txext)
            }
        }
    }

    fun getAllClipboardList(): Job = viewModelScope.launch {
        Log.e("polaris428","가져오기")
        getClipboardAllUseCase.execute(onComplete = {}).collect {
            _clipboardDataList.value = it
            Log.e("polaris428",it.toString())
        }
        Log.d("polaris428",clipboardDataList.value.toString())
    }

    fun postClipboardInsert(text: String): Job = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = MainUiState.ClipboardSaved
        val clipboardItem = if (isUrl(text)) {
            ClipboardItem(
                type = "web",
                url = text,
                title = fetchWebTitle(text).toString(),
                faviconUrl = getGoogleFaviconUrl(text)
            )
        } else {
            ClipboardItem(
                type = "text",
                url = null,
                title = text,
                faviconUrl = null
            )
        }

        postClipboardInsertUseCase.execute(
            item = clipboardItem,
            onComplete = {

            }).collect {

        }
        processIntent(MainIntent.getAllClipboardListIntent)
    }
}
