package com.polaris.main_save

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard.navigation.ClipboardRoute
import com.polaris.clipboard.navigation.clipboardNavGraph
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_save_animation.navigation.clipboardSaveAnimation
import com.polaris.clipboard_save_animation.navigation.navigateClipboardSaveAnimation
import com.polaris.main_save.state.ClipboardSaveState.*
import com.polaris.model.model.ClipboardItem
import com.polaris.util.PrefManager

@AndroidEntryPoint
class ClipboardSaveActivity : AppCompatActivity() {
    private val viewModel: ClipboardSaveViewModel by viewModels()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {


        }
        setContent {


            navController = rememberNavController()
            val state = viewModel.uiState.collectAsState()
            val clipboardItem = viewModel.clipboardItem.collectAsState()
            val folderNameList = viewModel.folderNameList.collectAsState()

            when (state.value) {
                Initialize -> {


                }

                ClipboardCrawlingInfo -> {

                }

                ClipboardSaveLoading -> {

                }

                ClipboardSaveSuccess -> {


                }

                ClipboardSaveFailure -> {

                }


            }

            NavHost(navController = navController, startDestination = ClipboardRoute.route) {
                clipboardNavGraph(
                    url = sharedText.toString(),
                    onSaveSuccess = {
                        navController.navigateClipboardSaveAnimation()
                    },
                    onEditClick = {
                        viewModel.updateClipboardItem(it)
                        navController.navigateClipboardEdit(it)
                    }, onDismiss = {

                        finish()

                    })
                clipboardEdit(
                    onSaveSuccess = {
                        navController.navigateClipboardSaveAnimation()
                    },
                )
                clipboardSaveAnimation(afterAnimation = {
                    Toast.makeText(this@ClipboardSaveActivity, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    exitProcess(0)  // 프로세스 종료
                })

            }

        }
    }

}
