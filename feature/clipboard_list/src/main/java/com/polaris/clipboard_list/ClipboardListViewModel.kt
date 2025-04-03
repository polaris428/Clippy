package com.polaris.clipboard_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.polaris.clipboard_list.intent.ClipboardListIntent
import com.polaris.clipboard_list.state.ClipboardState
import com.polaris.designsystem.ui.theme.dummyData
import kotlinx.coroutines.launch
import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardDeleteUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.UpdateClipboardPinStateUseCase
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    private val _uiState = MutableStateFlow(ClipboardState())
    val uiState: StateFlow<ClipboardState> = _uiState.asStateFlow()

    private var _folderList = MutableStateFlow< List<ClipboardFolder>>(emptyList())
    var folderList: StateFlow<List<ClipboardFolder>> = _folderList

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
            is ClipboardListIntent.LoadInitialFolders -> {
                _folderList.value = intent.folders

            }
            is ClipboardListIntent.ItemClicked -> {
                // URL 열기 등의 직접 동작은 UI에서 하고, 상태는 굳이 바꾸지 않음
            }

            is ClipboardListIntent.ItemLongPressed -> {
                _uiState.update { it.copy(
                    selectedItem = intent.item,
                    isSheetOpen = true
                )}
            }

            is ClipboardListIntent.Delete -> {
                postClipboardDelete(itemId =  intent.itemId, folderId =  intent.folderId)
                _uiState.update {
                    val newFolders = folderList.value.toMutableList()
                    val currentFolder = newFolders.getOrNull(it.currentIndex)
                    if (currentFolder != null) {
                        val newClipboards = currentFolder.clipboardDateList.filterNot { item ->
                            item.itemId == intent.itemId
                        }
                        newFolders[it.currentIndex] = currentFolder.copy(clipboardDateList = newClipboards)
                    }
                    updatefolders(newFolders)
                    it.copy(
                        selectedItem = null,
                        isSheetOpen = false
                    )
                }
            }

            is ClipboardListIntent.TogglePin -> {
                updateClipboardPinState(folderId = intent.folderId,itemId = intent.itemtId, pinState = intent.currentPin)
                _uiState.update { currentState ->


                    val newFolders = folderList.value.toMutableList()
                    val currentFolder = newFolders.getOrNull(currentState.currentIndex)
                    if (currentFolder != null) {

                        val newClipboards = currentFolder.clipboardDateList.map { item ->
                            if (item.itemId == intent.itemtId)
                                item.copy(isPinned = !intent.currentPin)
                            else item
                        }

                        newFolders[currentState.currentIndex] =
                            currentFolder.copy(clipboardDateList = newClipboards)
                    }
                    updatefolders(newFolders)

                    currentState.copy()
                }

            }

            is ClipboardListIntent.IndexUpdate -> {
                val index =folderList.value.indexOfFirst { it.id == intent.index }
                _uiState.update { it.copy(currentIndex = index) }
            }

            ClipboardListIntent.BottomSheetDismissed -> {
                _uiState.update { it.copy(isSheetOpen = false) }
            }

            ClipboardListIntent.ClearSelection -> {
                _uiState.update { it.copy(selectedItem = null) }
            }


        }
    }

    fun updatefolders(list:List<ClipboardFolder>){
        _folderList.value= list
    }
    fun clearSelectedClipboard(){
        _selectedClipboardItem.value = dummyData
    }

    fun postClipboardDelete(  folderId: String, itemId:String,) = viewModelScope.launch(Dispatchers.IO) {
        postClipboardDeleteUseCase.execute(folderId = folderId,itemId= itemId) {

        }.collect({ result ->


        })
    }

    fun updateClipboardPinState(folderId:String,itemId: String, pinState: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        updateClipboardPinStateUseCase.execute(folderId =folderId ,itemId = itemId, pinState = !pinState) {

        }.collect({

        })
    }

}