package com.polaris.folder_join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.domin.usecase.clipboard.folder.PostJoinFolderUseCase
import com.polaris.folder_join.intent.FolderJoinIntent
import com.polaris.folder_join.state.FolderJoinState
import com.polaris.model.model.ClipboardFolder
import com.polaris.util.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderJoinInViewModel @Inject constructor(
    private val postJoinFolderUseCase: PostJoinFolderUseCase
) : ViewModel() {


    private val _intent = MutableSharedFlow<FolderJoinIntent>()
    val intent: SharedFlow<FolderJoinIntent> = _intent.asSharedFlow()


    private val _uiState = MutableStateFlow<FolderJoinState>(FolderJoinState.Initialize)
    val uiState: StateFlow<FolderJoinState> get() = _uiState
    init {
        handleIntent()
    }
    fun updateUiState(signInState: FolderJoinState) {
        viewModelScope.launch {
            _uiState.emit(signInState)
        }
    }
    fun sendIntent(intent: FolderJoinIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is FolderJoinIntent.postFolderJoinIntent -> {
                        updateUiState(FolderJoinState.PostFolderLoading)
                        joinPolderJoin(intent.folderId)
                    }

                }
            }
        }
    }

   private fun joinPolderJoin(id:String)=viewModelScope.launch{

       postJoinFolderUseCase.execute(PrefManager.folderIdList,id).collect{
           if (it){
               updateUiState(FolderJoinState.PostFolderSuccess)
           }else{
               updateUiState(FolderJoinState.PostFolderFail(501))
           }


       }
   }
}

