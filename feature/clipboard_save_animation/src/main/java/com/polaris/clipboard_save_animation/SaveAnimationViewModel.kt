package com.polaris.clipboard_save_animation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard_save_animation.intent.SaveAnimationSeenIntent
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
class SaveAnimationViewModel @Inject constructor(
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
): ViewModel() {

    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem : StateFlow<ClipboardItem> = _clipboardItem

    fun updateClipboardItem(clipboardItem: ClipboardItem){
        _clipboardItem.value = clipboardItem
    }

    fun processIntent(intent: SaveAnimationSeenIntent) {
        when (intent) {


            is SaveAnimationSeenIntent.postClipboarInsertIntent -> {
                postClipboardInsert()
            }
        }
    }

    fun postClipboardInsert(): Job = viewModelScope.launch(Dispatchers.IO) {



        postClipboardInsertUseCase.execute(
            item = clipboardItem.value,
            onComplete = {

            }).collect {

        }

    }



}