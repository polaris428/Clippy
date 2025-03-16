package com.polaris.sign_in.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.domin.usecase.clipboard.folder.PostFolderUseCase
import com.polaris.model.ClipboardFolder
import com.polaris.sign_in.intent.SignInIntent
import com.polaris.util.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val postFolderUseCase: PostFolderUseCase) : ViewModel() {
    fun processIntent(intent: SignInIntent) {
        when(intent){
            is SignInIntent.postInitFolderIntent->{
                postBaseFolder()
            }
        }
    }

    fun postBaseFolder()=viewModelScope.launch{

        val initFolder = ClipboardFolder()

        postFolderUseCase.execute(PrefManager.userSignInCheck,initFolder).collect{

        }

    }

}