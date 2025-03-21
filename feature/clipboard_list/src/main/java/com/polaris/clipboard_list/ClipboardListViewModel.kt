package com.polaris.clipboard_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardDeleteUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.UpdateClipboardPinStateUseCase
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ClipboardListViewModel @Inject constructor(
    private val postClipboardDeleteUseCase: PostClipboardDeleteUseCase,
    private val updateClipboardPinStateUseCase: UpdateClipboardPinStateUseCase
) : ViewModel() {

    // 바텀시트 열림 상태 (초기값 false)
    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen: StateFlow<Boolean> = _isSheetOpen

    // 선택된 ClipboardItem (기본값 dummyData)
    private val _selectedClipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val selectedClipboardItem: StateFlow<ClipboardItem> = _selectedClipboardItem

    private val _index = MutableStateFlow<Int>(0)
    val index: StateFlow<Int> = _index


    fun  indexUpdate(index:Int){
        _index.value= index
    }

    /**
     * 특정 아이템 롱클릭 시 바텀 시트를 열고 선택한 아이템 저장
     */
    fun onItemLongPressed(item: ClipboardItem) {
        _selectedClipboardItem.value = item
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
                _isSheetOpen.value = true
                _selectedClipboardItem.value = intent.item

            }

            is ClipboardListIntent.postClipboardDeleteIntent -> {
                postClipboardDelete(timestamp = intent.timestamp)
            }

            is ClipboardListIntent.UpdatePinClipboardDeleteIntent -> {
                updateClipboardPinState(timestamp = intent.timestamp ,intent.pinState)
            }

            is ClipboardListIntent.BottomSheetDismissed -> {
                _isSheetOpen.value = false

            }

        }
    }



    fun clearSelectedClipboard(){
        _selectedClipboardItem.value = dummyData
    }

    fun postClipboardDelete(timestamp: Long) = viewModelScope.launch(Dispatchers.IO) {
        postClipboardDeleteUseCase.execute(timestamp = timestamp) {

        }.collect({ result ->


        })
    }

    fun updateClipboardPinState(timestamp: Long, pinState: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        updateClipboardPinStateUseCase.execute(timestamp = timestamp, pinState = !pinState) {

        }.collect({

        })
    }

}