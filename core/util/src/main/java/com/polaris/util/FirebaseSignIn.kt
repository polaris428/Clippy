package com.polaris.util

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class GoogleSignInHelper(
    val activity: ComponentActivity,
    private val onSignInSuccess: (Task<AuthResult>) -> Unit,
    private val onSignInFailure: (Exception) -> Unit
) {
    private val googleSignInClient: GoogleSignInClient

    init {
        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken("918882902549-m90l96ejovbhg9q567liin4qafj4toba.apps.googleusercontent.com")
            .requestEmail()
            .build()

         googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
    }



    private val activityResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    onSignInFailure(e)
                }
            } else {
                onSignInFailure(Exception("Google Sign-In canceled with code ${result.resultCode}"))
            }
        }

    fun startGoogleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent //구글로그인 페이지로 가는 인텐트 객체

        activityResultLauncher.launch(signInIntent)
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {

                        onSignInSuccess(task)




                    } else {
                      onSignInFailure(Exception(task.exception))

                    }
                })
    }


    fun googleSignOut(){

        FirebaseAuth.getInstance().signOut()
        PrefManager.userSignInCheck = false
        PrefManager.userSignInSkip = false
        PrefManager.userUid = ""
    }

    fun signInAnonymously(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnSuccessListener { authResult ->
                val userId = authResult.user!!.uid
                onSuccess(userId)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "익명 로그인 실패")
            }
    }




}