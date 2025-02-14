package com.polaris.clipboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard.intent.ClipboardIntent
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
import javax.inject.Inject

@HiltViewModel
class ClipboardViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
): ViewModel() {


    fun processIntent(intent: ClipboardIntent) {
        when (intent) {


            is ClipboardIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.txext)
            }
        }
    }

    fun postClipboardInsert(url: String): Job = viewModelScope.launch(Dispatchers.IO) {
        Log.e("polaris0428++",  getMetaDescription(url))

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
