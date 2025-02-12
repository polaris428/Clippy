package com.polaris.clipboard

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

    fun postClipboardInsert(text: String): Job = viewModelScope.launch(Dispatchers.IO) {

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

    }
}