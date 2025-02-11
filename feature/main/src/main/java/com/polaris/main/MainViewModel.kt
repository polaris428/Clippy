package com.polaris.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val getClipboardAllUseCase: GetClipboardAllUseCase,
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
) : ViewModel() {
    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.getAllClipboardListIntent -> {
                getAllClipboardList()
            }

            is MainIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.clipboardItem)
            }
        }
    }

    fun getAllClipboardList(): Job = viewModelScope.launch {
        Log.e("polaris428","가져오기")
        getClipboardAllUseCase.execute(onComplete = {}).collect {
            Log.e("polaris428",it.toString())
        }
    }

    fun postClipboardInsert(item: ClipboardItem): Job = viewModelScope.launch(Dispatchers.IO) {
        postClipboardInsertUseCase.execute(
            item = item,
            onComplete = {

            }).collect {

        }
    }
}
