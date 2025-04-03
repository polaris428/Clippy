package com.polaris.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.remote.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.PostClipboardInsertUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.PostDataMigrationUseCase
import com.polaris.domin.usecase.clipboard.remote.clipboard.UpdateClipboardUseCase
import com.polaris.domin.usecase.clipboard.remote.folder.PostFolderSyncUseCase
import com.polaris.main.state.MainState
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.sign_in.intent.SignInIntent
import com.polaris.sign_in.state.SignInState
import com.polaris.splash.state.SplashState
import com.polaris.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

    private val getClipboardAllFolderUseCase: GetClipboardAllUseCase,
    private val postClipboardMigrationUseCase: PostDataMigrationUseCase,
    private val postFolderSyncUseCase: PostFolderSyncUseCase,



) : ViewModel() {


    private val _intent = MutableSharedFlow<MainIntent>()
    val intent: SharedFlow<MainIntent> = _intent.asSharedFlow()
    private val _clipboardItem = MutableStateFlow<ClipboardItem>(ClipboardItem())
    val clipboardItem: StateFlow<ClipboardItem> = _clipboardItem

    private val _clipboardDataList = MutableStateFlow<List<ClipboardFolder>>(listOf())
    val clipboardDataList: StateFlow<List<ClipboardFolder>> = _clipboardDataList


    init {
        handleIntent()
    }




    fun sendIntent(intent: MainIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }



    fun mainSetClipboardDataList(clipboardFolderList:List<ClipboardFolder>){
        Log.e("polaris",clipboardFolderList.toJson())
        _clipboardDataList.value = clipboardFolderList
    }
    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is MainIntent.getAllClipboardListIntent ->{
                        getClipboardFolder(intent.folderIdList)

                    }

                    else -> {}
                }
            }
        }
    }





    fun updateClipboardItem(item: ClipboardItem) {
        _clipboardItem.value = item
    }

    fun getClipboardFolder(folderId: List<String>): Job = viewModelScope.launch {

        getClipboardAllFolderUseCase.execute(folderId).collect {
            _clipboardDataList.value = it


        }


    }

    fun postClipboardMigrationUseCase(clipboardList: List<ClipboardItem>): Job =
        viewModelScope.launch {
            postClipboardMigrationUseCase.execute(clipboardList, onComplete = {}).collect {

            }
        }


    fun postLocalFolder(
        oldFolders: List<ClipboardFolder>,
        newFolders: List<ClipboardFolder>
    ) = viewModelScope.launch {
        postFolderSyncUseCase.execute(oldFolders = oldFolders, newFolders = newFolders).collect {

        }
    }




}
