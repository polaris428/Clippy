package com.polaris.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.clipboard.GetClipboardFolderUseCase
import com.polaris.domin.usecase.clipboard.clipboard.PostClipboardInsertUseCase
import com.polaris.domin.usecase.clipboard.clipboard.PostDataMigrationUseCase
import com.polaris.domin.usecase.clipboard.clipboard.UpdateClipboardUseCase
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.shared.intent.MainIntent
import com.polaris.util.PrefManager
import com.polaris.util.extractUrl
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.getWebTitle
import com.polaris.util.isUrl
import com.polaris.util.toJson
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
    private val getClipboardFolderUseCase: GetClipboardFolderUseCase,

    private val postClipboardInsertUseCase: PostClipboardInsertUseCase,
    private val updateClipboardUseCase: UpdateClipboardUseCase,
    private val postClipboardMigrationUseCase: PostDataMigrationUseCase,


    ) : ViewModel() {


    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem

    private val _clipboardDataList = MutableStateFlow<ClipboardFolder>(ClipboardFolder())
    val clipboardDataList: StateFlow<ClipboardFolder> = _clipboardDataList

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.getAllClipboardListIntent -> {
                getClipboardFolder(intent.folderId)
            }

            is MainIntent.postClipboarInsertIntent -> {
                postClipboardInsert(PrefManager.folderIdList[0])
            }

            is MainIntent.updateClipboarIntent -> {
                updateClipboard()
            }
            is MainIntent.postClipboardMigrationUseCase ->{
                postClipboardMigrationUseCase(intent.list)
            }


        }
    }

    fun getClipboardFolder(folderId: String): Job = viewModelScope.launch {

        getClipboardFolderUseCase.execute(folderId).collect {
            Log.e("polaris040428",it.toJson())
            _clipboardDataList.value = it
        }

    }

    fun postClipboardInsert(id:String): Job = viewModelScope.launch(Dispatchers.IO) {


        postClipboardInsertUseCase.execute(
            folderId = id,
            item = clipboardItem.value,
            isLogin = PrefManager.userSignInCheck,
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

    fun updateClipboardItem(item: ClipboardItem) {
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

                processIntent(MainIntent.getAllClipboardListIntent(PrefManager.folderIdList[0]))
            }


    }

    fun postClipboardMigrationUseCase(clipboardList: List<ClipboardItem>):Job = viewModelScope.launch {
        postClipboardMigrationUseCase.execute(clipboardList, onComplete = {}).collect{

        }
    }


}
