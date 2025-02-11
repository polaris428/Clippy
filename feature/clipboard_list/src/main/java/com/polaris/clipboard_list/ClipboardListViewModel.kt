package com.polaris.clipboard_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.polaris.domin.usecase.clipboard.GetClipboardAllUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polaris.data.local.ClipboardItem
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase

@HiltViewModel
class ClipboardListViewModel @Inject constructor(
    private val getClipboardAllUseCase: GetClipboardAllUseCase,
    private val postClipboardInsertUseCase: PostClipboardInsertUseCase
) : ViewModel() {
    fun processIntent(intent: ClipboardListIntent) {
        when (intent) {
            is ClipboardListIntent.getAllClipboardListIntent -> {
                getAllClipboardList()
            }

            is ClipboardListIntent.postClipboarInsertIntent -> {
                postClipboardInsert(intent.clipboardItem)
            }
        }
    }

    fun getAllClipboardList(): Job = viewModelScope.launch {
        getClipboardAllUseCase.execute(onComplete = {}).collect {

        }
    }

    fun postClipboardInsert(item: ClipboardItem): Job = viewModelScope.launch {
        postClipboardInsertUseCase.execute(
            item = item,
            onComplete = {

            }).collect {

        }
    }

}