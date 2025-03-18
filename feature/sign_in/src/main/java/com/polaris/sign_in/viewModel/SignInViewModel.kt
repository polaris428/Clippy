package com.polaris.sign_in.viewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.domin.usecase.clipboard.user.GetCheckIfUserExistsUseCase
import com.polaris.domin.usecase.clipboard.user.PostUserInfoUseCase
import com.polaris.model.dto.UserDTO
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.User
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
    private val postUserInfoUseCase: PostUserInfoUseCase,
    private val getCheckIfUserExistsUseCase: GetCheckIfUserExistsUseCase
) :
    ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<SignInState>(SignInState.Initialize)
    val uiState: StateFlow<SignInState> get() = _uiState

    private val _intent = MutableSharedFlow<SignInIntent>()
    val intent: SharedFlow<SignInIntent> = _intent.asSharedFlow()


    init {
      handleIntent()
    }
    fun updateState(signInState: SignInState) {
        viewModelScope.launch {
            _uiState.emit(signInState)  // 🔥 `emit()` 사용
        }
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
                    is SignInIntent.PostUserInfoIntent -> postUserInfo(intent.user)
                    is SignInIntent.GetCheckIfUserExists -> getCheckIfUserExists(intent.uid)
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

    fun postUserInfo(user: User) = viewModelScope.launch {

        postUserInfoUseCase.execute(user).collect {

        }
    }

    fun getCheckIfUserExists(uid:String) = viewModelScope.launch {
        getCheckIfUserExistsUseCase.execute(uid).collect{

        }
    }
    fun handleSignInResult(intent: Intent?) {
        try {
            val idToken = intent?.getStringExtra("idToken") ?: return
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            viewModelScope.launch {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e("FirebaseAuth", "로그인 성공")
                           // _user.value = auth.currentUser
                        } else {
                           Log.e("FirebaseAuth", "로그인 실패", task.exception)
                        }
                    }
            }
        } catch (e: Exception) {
          //  Log.e("GoogleSignIn", "Google 로그인 실패", e)
        }
    }


}