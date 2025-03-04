package com.polaris.clipboard_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard_edit.intent.ClipboardEditIntent
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClipboardEditViewModel  @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
): ViewModel() {

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem : StateFlow<ClipboardItem> = _clipboardItem

    fun updateClipboardItem(clipboardItem: ClipboardItem){
        _clipboardItem.value = clipboardItem
    }

    fun processIntent(intent: ClipboardEditIntent) {
        when (intent) {


            is ClipboardEditIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.txext)
            }
        }
    }

    fun postClipboardInsert(url: String): Job = viewModelScope.launch(Dispatchers.IO) {



        postClipboardInsertUseCase.execute(
            item = clipboardItem.value,
            onComplete = {

            }).collect {

        }

    }



}