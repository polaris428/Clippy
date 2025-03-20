package com.polaris.folder_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.clipboard.GetClipboardAllUseCase
import com.polaris.domin.usecase.clipboard.clipboard.PostClipboardInsertUseCase
import com.polaris.domin.usecase.clipboard.clipboard.PostDataMigrationUseCase
import com.polaris.domin.usecase.clipboard.clipboard.UpdateClipboardUseCase
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.folder_edit.intent.FolderEditIntent
import com.polaris.folder_edit.state.FolderEditState
import com.polaris.model.model.ClipboardFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderEditViewModel @Inject constructor(
    private val postFolderUseCase: PostFolderUseCase
) : ViewModel() {


    private val _intent = MutableSharedFlow<FolderEditIntent>()
    val intent: SharedFlow<FolderEditIntent> = _intent.asSharedFlow()


    private val _uiState = MutableStateFlow<FolderEditState>(FolderEditState.Initialize)
    val uiState: StateFlow<FolderEditState> get() = _uiState
    init {
        handleIntent()
    }
    fun updateUiState(signInState: FolderEditState) {
        viewModelScope.launch {
            _uiState.emit(signInState)
        }
    }
    fun sendIntent(intent: FolderEditIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is FolderEditIntent.postFolderIntent -> {
                        updateUiState(FolderEditState.PostFolderLoading)
                        postFolder(intent.clipboardFolder)
                    }

                }
            }
        }
    }

    private fun postFolder(clipboardFolder: ClipboardFolder) = viewModelScope.launch {
        postFolderUseCase.execute(clipboardFolder).collect {
            updateUiState(FolderEditState.PostFolderSuccess)
        }
    }
}

