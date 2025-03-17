package com.polaris.sign_in.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.domin.usecase.clipboard.user.PostUserInfoUseCase
import com.polaris.model.dto.UserDTO
import com.polaris.model.model.ClipboardFolder
import com.polaris.sign_in.intent.SignInIntent
import com.polaris.sign_in.state.SignInState
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
class SignInViewModel @Inject constructor(
    private val postFolderUseCase: PostFolderUseCase,
    private val postUserInfoUseCase: PostUserInfoUseCase
) :
    ViewModel() {


    private val _uiState = MutableStateFlow<SignInState>(SignInState.Idle)
    val uiState: StateFlow<SignInState> get() = _uiState

    private val _intent = MutableSharedFlow<SignInIntent>()
    val intent: SharedFlow<SignInIntent> = _intent.asSharedFlow()


    init {
      handleIntent()
    }
    fun sendIntent(intent: SignInIntent) {
        viewModelScope.launch {
            _intent.emit(intent)  // 🔥 `emit()` 사용
        }
    }
    private fun handleIntent() {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is SignInIntent.PostUserInfoIntent -> postUserInfo(intent.userDTO)
                    is SignInIntent.PostInitFolderIntent -> postBaseFolder()
                }
            }
        }
    }


    fun postBaseFolder() = viewModelScope.launch {

        val initFolder = ClipboardFolder(owner = PrefManager.userUid)

        postFolderUseCase.execute(PrefManager.userSignInCheck, initFolder).collect {

        }

    }

    fun postUserInfo(userDTO: UserDTO) = viewModelScope.launch {

        postUserInfoUseCase.execute(userDTO).collect {

        }
    }

}