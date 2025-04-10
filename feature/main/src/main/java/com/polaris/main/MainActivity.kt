package com.polaris.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_list.navigation.clipboardListNavGraph
import com.polaris.clipboard_list.navigation.navigateClipboardList
import com.polaris.folder_edit.navigation.folderEditNavGraph
import com.polaris.folder_edit.navigation.navigateFolderEdit
import com.polaris.folder_join.navigation.folderJoinNavGraph
import com.polaris.folder_join.navigation.navigateFolderJoin
import com.polaris.main.state.MainState
import com.polaris.model.model.ClipboardItem

import com.polaris.sign_in.navigation.navigateSignIn
import com.polaris.sign_in.navigation.signInNavGraph
import com.polaris.splash.navigation.SplashRoute
import com.polaris.splash.navigation.navigateSplash
import com.polaris.splash.navigation.splashNavGraph
import com.polaris.util.GoogleSignInHelper
import com.polaris.util.PrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var googleSignInHelper: GoogleSignInHelper


    lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initGoogleSignInHelper()


        setContent {

            val viewModel: MainViewModel by viewModels()
            val clipboardDataList by viewModel.clipboardDataList.collectAsState()

            navController = rememberNavController()

            LaunchedEffect(clipboardDataList) {
                Log.e("poalris0428","새로고침됨")
                if (clipboardDataList.isNotEmpty()) {
                    navController.navigateClipboardList()
                }
            }

            NavHost(navController = navController, startDestination = SplashRoute.route) {
                splashNavGraph(onSplashCompleted = {
                    viewModel.mainSetClipboardDataList(it)

                    if (!PrefManager.userSignInSkip) {
                        navController.navigateSignIn()

                    }

                }

                )
                signInNavGraph(
                    googleSignInHelper = googleSignInHelper,
                    onSignIncomplete = {
                        PrefManager.userSignInSkip = true
                        navController.navigateSplash()


                    })
                clipboardListNavGraph(
                    clipboardDataList,
                    onEditClick = { item: ClipboardItem ->
                        viewModel.updateClipboardItem(item)
                        navController.navigateClipboardEdit(item)

                    }, onAddFolderClick = {
                        navController.navigateFolderEdit()
                    },
                    onJoinFolderClick = {
                        navController.navigateFolderJoin()
                    },
                    onSettingClick = {

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"  // 공유할 데이터 타입
                            putExtra(Intent.EXTRA_TEXT,"폴더를 참가하기를 통해 ID를 입력해주세요" + it.id)  // 실제 공유할 내용
                        }

                        val chooser = Intent.createChooser(shareIntent, "앱을 선택해주세요")  // 시스템 바텀 시트(공유 패널) 생성
                        startActivity(chooser)
                    })

                clipboardEdit(
                    onSaveSuccess = {
                        viewModel.sendIntent(MainIntent.getAllClipboardListIntent(PrefManager.folderIdList))
                    })

                folderEditNavGraph(onPostFolderSuccess = {
                    viewModel.sendIntent(MainIntent.getAllClipboardListIntent(PrefManager.folderIdList))
                   // navController.navigateClipboardList()
                }, onPostFolderFile = {
                    navController.navigateClipboardList()
                })
                folderJoinNavGraph(onPostFolderSuccess = {}, onPostFolderFile = {})


            }


        }
    }

    private fun initGoogleSignInHelper() {
        googleSignInHelper = GoogleSignInHelper(activity = this, onSignInSuccess = { task ->
            PrefManager.userSignInCheck = true
            PrefManager.userUid = task.result.user!!.uid



        }, onSignInFailure = { exception ->
            Toast.makeText(this, "로그인에 실패했어요, 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        })

    }

    fun updateClipDate(type: String, title: String) {
        // viewModel.updateClipboardItem(type, title)
    }




}


