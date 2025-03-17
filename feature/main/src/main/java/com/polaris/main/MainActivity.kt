package com.polaris.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_list.navigation.clipboardListNavGraph
import com.polaris.clipboard_list.navigation.navigateClipboardList
import com.polaris.model.dto.UserDTO
import com.polaris.model.model.ClipboardItem
import com.polaris.shared.MainViewModel
import com.polaris.shared.intent.MainIntent
import com.polaris.sign_in.intent.SignInIntent
import com.polaris.sign_in.navigation.navigateSignIn
import com.polaris.sign_in.navigation.signInNavGraph
import com.polaris.splash.navigation.SplashRoute
import com.polaris.splash.navigation.splashNavGraph
import com.polaris.util.GoogleSignInHelper
import com.polaris.util.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private val viewModel: MainViewModel by viewModels()
    lateinit var navController: NavHostController
    var dummyDate = listOf(
        ClipboardItem(
            type = "구글",
            url = "https://www.google.com/search?q=%EB%84%A4%EC%9D%B4%EB%B2%84&oq=&gs_lcrp=...",
            title = "Google",
            faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://www.google.com/search?q=%EB%84%A4%EC%9D%B4%EB%B2%84&oq=&gs_lcrp=...",
            timestamp = 1740930924019,
            isPinned = false
        ),
        ClipboardItem(
            type = "네이버 포스트",
            url = "https://m.post.naver.com/viewer/postView.naver?volumeNo=38357237&memberNo=57847637",
            title = "[24년도] 콘서트 망원경 이 글 하나로 종결 : 네이버 포스트",
            faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://m.post.naver.com/viewer/postView.naver?volumeNo=38357237&memberNo=57847637",
            timestamp = 1741153750278,
            isPinned = false
        ),
        ClipboardItem(
            type = "ㅇㅅㅇ",
            url = "https://m.search.naver.com/search.naver?query=88%ED%8F%AC%EC%B0%A8+%EB%A9%94%EB%89%B4",
            title = "88포차 메뉴 : 네이버 검색",
            faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://m.search.naver.com/search.naver?query=88%ED%8F%AC%EC%B0%A8+%EB%A9%94%EB%89%B4",
            timestamp = 1741157005289,
            isPinned = false
        ),
        ClipboardItem(
            type = "Google",
            url = "https://www.google.com/search?client=ms-android-samsung-ss&q=lindor+%EC%B4%88%EC%BD%9C%EB%A6%BF+%EC%95%8C%EC%BD%9C",
            title = "lindor 초콜릿 알콜 - Google 검색",
            faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://www.google.com/search?client=ms-android-samsung-ss&q=lindor+%EC%B4%88%EC%BD%9C%EB%A6%BF+%EC%95%8C%EC%BD%9C",
            timestamp = 1741264638943,
            isPinned = false
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInHelper = GoogleSignInHelper(activity = this, onSignInSuccess = { task ->
            Log.d("polaris0428", "성공")
            PrefManager.userSignInCheck = true
            PrefManager.userUid = task.result.user!!.uid
            if (PrefManager.userUid != "") {

                viewModel.postClipboardMigrationUseCase(viewModel.clipboardDataList.value)
            }


        }, onSignInFailure = { exception ->
            Toast.makeText(this, "로그인에 실패했어요, 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        })
        //googleSignInHelper.googleSignOut()

        setContent {

            navController = rememberNavController()


            NavHost(navController = navController, startDestination = SplashRoute.route) {
                splashNavGraph(viewModel, onSplashCompleted = {
                    if (!PrefManager.userSignInSkip) {
                        dummyDate.forEach {
                            viewModel.postInsertDummyData(dummyDate)
                        }
                        navController.navigateClipboardList()
                    } else {
                        navController.navigateSignIn()
                    }

                }

                )
                signInNavGraph(
                    onSignInClick = { googleSignInHelper.startGoogleSignIn() },
                    googleSignInHelper=googleSignInHelper,
                    onSignIncomplete = {
                        PrefManager.userSignInSkip = true
                        navController.navigateClipboardList()


                    })
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

    fun generateGuestId(): String {
        val uniqueNumber = UUID.randomUUID().hashCode() and Int.MAX_VALUE  // 양수로 변환
        return "guest$uniqueNumber"
    }


}


