package com.polaris.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_list.navigation.clipboardListNavGraph
import com.polaris.clipboard_list.navigation.navigateClipboardList
import com.polaris.data.local.ClipboardItem
import com.polaris.shared.MainViewModel
import com.polaris.shared.intent.MainIntent
import com.polaris.sign_in.navigation.navigateSignIn
import com.polaris.sign_in.navigation.signInNavGraph
import com.polaris.splash.navigation.SplashRoute
import com.polaris.splash.navigation.splashNavGraph
import com.polaris.util.GoogleSignInHelper
import com.polaris.util.PrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private val viewModel: MainViewModel by viewModels()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInHelper = GoogleSignInHelper(activity = this, onSignInSuccess = { task ->
            Log.d("polaris0428", "성공")
            PrefManager.userSignInCheck = true
            PrefManager.userUid = task.result.user!!.uid
            navController.navigateClipboardList()


        }, onSignInFailure = { exception ->
            Toast.makeText(this, "로그인에 실패했어요, 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        })
        viewModel.processIntent(MainIntent.getAllClipboardListIntent)
        setContent {

            navController = rememberNavController()


            NavHost(navController = navController, startDestination = SplashRoute.route) {
                splashNavGraph(onSplashCompleted = {
                    if (PrefManager.userSignInCheck) {
                        navController.navigateClipboardList()
                    } else {
                        navController.navigateSignIn()
                    }

                }

                )
                signInNavGraph(onSignInClick = { googleSignInHelper.startGoogleSignIn() })
                clipboardListNavGraph(
                    viewModel.clipboardDataList,
                    onEditClick = { item: ClipboardItem ->
                        viewModel.updateClipboardItem(item)
                        navController.navigateClipboardEdit()

                    })

                clipboardEdit(mainViewModel = viewModel, onSaveClick = { type, title ->
                    updateClipDate(type, title)
                    saveClipboard()
                })

            }


        }
    }

    fun updateClipDate(type: String, title: String) {
        viewModel.updateClipboardItem(type, title)
    }


    fun saveClipboard() {


        viewModel.processIntent(MainIntent.updateClipboarIntent)

        navController.navigateClipboardList()

    }

}


