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
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import com.polaris.util.extractUrl
import com.polaris.util.getMetaDescription
import com.polaris.util.getWebTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ClipboardViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<ClipboardUiState>(ClipboardUiState.Initialize)
    val uiState: StateFlow<ClipboardUiState> = _uiState

    fun processIntent(intent: ClipboardIntent) {
        when (intent) {


            is ClipboardIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.txext)
            }
        }
    }

    suspend fun siteInformation(url: String): Job = viewModelScope.launch {
        val clipboardItem = if (isUrl(url)) {
            val urlPreprocessing = extractUrl(url)
            ClipboardItem(
                type = getWebTitle(urlPreprocessing),
                url = urlPreprocessing,
                title = fetchWebTitle(urlPreprocessing).toString(),
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


    }

    fun postClipboardInsert(url: String): Job = viewModelScope.launch(Dispatchers.IO) {


        val clipboardItem = if (isUrl(url)) {
            val urlPreprocessing = extractUrl(url)
            ClipboardItem(
                type = getWebTitle(urlPreprocessing),
                url = urlPreprocessing,
                title = fetchWebTitle(urlPreprocessing).toString(),
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

        postClipboardInsertUseCase.execute(
            item = clipboardItem,
            onComplete = {

            }).collect {

        }

    }
}
