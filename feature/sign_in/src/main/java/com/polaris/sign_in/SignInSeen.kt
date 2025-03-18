package com.polaris.sign_in

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.Gray40
import com.polaris.model.model.User
import com.polaris.sign_in.intent.SignInIntent
import com.polaris.sign_in.state.SignInState
import com.polaris.sign_in.viewModel.SignInViewModel
import com.polaris.util.GoogleSignInHelper
import com.polaris.util.PrefManager
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.polaris.model.model.ClipboardFolder

@Composable
fun SignInSeen(
    googleSignInHelper: GoogleSignInHelper,
    onSignIncomplete: (ClipboardFolder) -> Unit
) {
    val viewModel: SignInViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsState()


    val context = LocalContext.current

    val oneTapClient: SignInClient = remember { Identity.getSignInClient(context) }
    val signInRequest: BeginSignInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("918882902549-m90l96ejovbhg9q567liin4qafj4toba.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                val intent = Intent().apply { putExtra("idToken", idToken) }
                viewModel.handleSignInResult(intent)
            }
        } catch (e: ApiException) {
            println("Google 로그인 실패: ${e.message}")
        }
    }



    when (state.value) {
        is SignInState.Initialize -> {

        }

        is SignInState.Loading -> {

        }

        is SignInState.SignInSuccess -> {
            viewModel.sendIntent(
                SignInIntent.PostUserInfoIntent(
                    user = User(
                        id = PrefManager.userUid,
                        folderList = arrayListOf()
                    )
                )
            )

        }

        is SignInState.Error -> {
            Toast.makeText(LocalContext.current, "오류가 발생했어요", Toast.LENGTH_SHORT).show()

        }
        is SignInState.Complete ->{

            onSignIncomplete((state.value as SignInState.Complete).clipboardFolder)
            viewModel.updateState(SignInState.Initialize)
        }
    }
    SignInView(
        onSignInClick = {

            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    googleSignInLauncher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                }
                .addOnFailureListener {
                    //TODO: UI 에러처리 필요
                    println("Google One Tap 로그인 실패: ${it.message}")
                }
        },
        onSignInAnonymouslyClick = {
            googleSignInHelper.signInAnonymously(
                onSuccess = {
                    viewModel.updateState(SignInState.SignInSuccess)
                }, onFailure = {

                })
        },

        )


}


@Composable
@Preview(showBackground = true)
fun SignInView(
    onSignInClick: () -> Unit = {},
    onSignInAnonymouslyClick: () -> Unit = {},

    ) {

    CDSColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Clippy",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    "복사 그 이상, 더 스마트한 클립보드.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )

            }
        }
        Text(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 효과 제거
            ) {
                onSignInAnonymouslyClick()
                //onSignIncomplete()
            },
            text = "로그인 없이 계속하기",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray40
        )
        Spacer(modifier = Modifier.height(12.dp))
        CDSButton(buttonText = "로그인 하기", onClick = {
            onSignInClick()
            // onSignIncomplete()
        })
    }
}


fun initGoogleSignIn(){}



