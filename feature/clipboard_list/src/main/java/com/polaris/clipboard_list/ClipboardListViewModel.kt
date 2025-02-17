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
import com.polaris.domin.usecase.clipboard.PostClipboardDeleteUseCase
import com.polaris.domin.usecase.clipboard.PostClipboardInsertUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ClipboardListViewModel @Inject constructor(
    private val postClipboardDeleteUseCase: PostClipboardDeleteUseCase
) : ViewModel() {

    // 바텀시트 열림 상태 (초기값 false)
    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen: StateFlow<Boolean> = _isSheetOpen

    // 선택된 ClipboardItem (기본값 dummyData)
    private val _selectedItem = MutableStateFlow(dummyData)
    val selectedItem: StateFlow<ClipboardItem> = _selectedItem

    /**
     * 특정 아이템 롱클릭 시 바텀 시트를 열고 선택한 아이템 저장
     */
    fun onItemLongPressed(item: ClipboardItem) {
        _selectedItem.value = item
        _isSheetOpen.value = true
    }

    /**
     * 바텀 시트 닫기
     */
    fun closeBottomSheet() {
        _isSheetOpen.value = false
    }



    fun processIntent(intent: ClipboardListIntent) {
        when (intent) {
            is ClipboardListIntent.ItemLongPressed -> {
                _selectedItem.value = intent.item
                _isSheetOpen.value = true
            }
            is ClipboardListIntent.postClipboardDeleteIntent -> {
                postClipboardDelete(id = intent.id)
            }
            ClipboardListIntent.BottomSheetDismissed -> {
                _isSheetOpen.value = false
            }

        }
    }

    fun postClipboardDelete(id:Int) = viewModelScope.launch  {
        postClipboardDeleteUseCase.execute(id = id){

        }
    }

}