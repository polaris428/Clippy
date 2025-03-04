package com.polaris.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
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
class MainViewModel@Inject constructor(
    private val getClipboardAllUseCase: GetClipboardAllUseCase,
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
) : ViewModel() {


    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem : StateFlow<ClipboardItem> = _clipboardItem

    private val _clipboardDataList = MutableStateFlow<List<ClipboardItem>>(emptyList())
    val clipboardDataList :StateFlow<List<ClipboardItem>> =  _clipboardDataList

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.getAllClipboardListIntent -> {
                getAllClipboardList()
            }
            is MainIntent.postClipboarInsertIntent ->{
                postClipboardInsert()
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
            val title = withContext(Dispatchers.IO){
                fetchWebTitle(urlPreprocessing)
            }
            Log.e("polaris0428",type)
            Log.e("polaris0428",title)
            ClipboardItem(
                type = type,
                url = urlPreprocessing,
                title = title?:"",
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
