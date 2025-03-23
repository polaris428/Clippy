package com.polaris.clipboard

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
import com.polaris.clipboard.intent.ClipboardSaveIntent
import com.polaris.clipboard.navigation.ClipboardRoute
import com.polaris.clipboard.navigation.clipboardNavGraph
import com.polaris.clipboard.state.ClipboardSaveState
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_save_animation.navigation.clipboardSaveAnimation
import com.polaris.clipboard_save_animation.navigation.navigateClipboardSaveAnimation
import com.polaris.model.model.ClipboardItem
import com.polaris.util.PrefManager

@AndroidEntryPoint
class ClipboardSaveActivity : AppCompatActivity() {
    private val viewModel: ClipboardSaveViewModel by viewModels()
    lateinit var navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {
            viewModel.sendIntent(ClipboardSaveIntent.getUrlCrawlingInfo(sharedText))


        }
        setContent {



            navController = rememberNavController()
            val state = viewModel.uiState.collectAsState()
            val clipboardItem= viewModel.clipboardItem.collectAsState()

            when (state.value) {
                ClipboardSaveState.Initialize ->{


                }

                ClipboardSaveState.ClipboardCrawlingInfo -> {

                }
                ClipboardSaveState.ClipboardSaveLoading -> {

                }
                ClipboardSaveState.ClipboardSaveSuccess -> {
                    navController.navigateClipboardSaveAnimation()
                    Toast.makeText(this@ClipboardSaveActivity, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()
                }
                ClipboardSaveState.ClipboardSaveFailure -> {

                }


            }

            NavHost(navController = navController, startDestination = ClipboardRoute.route ) {
                clipboardNavGraph(
                    title =clipboardItem.value.title,
                    siteName =clipboardItem.value.type,
                    onSaveClick = {
                        saveClipboard(clipboardItem.value)

                    }, onEditClick = {
                        navController.navigateClipboardEdit(clipboardItem.value)
                    }, onDismiss = {

                    })
                clipboardEdit( onSaveClick = { type, title ->
                    viewModel.updateClipboardItem(type = type , title= title)
                    saveClipboard(clipboardItem.value)

                })
                clipboardSaveAnimation(afterAnimation = {
                    finish()
                    exitProcess(0)  // 프로세스 종료
                })

            }

        }
    }

    fun saveClipboard(ClipboardItem: ClipboardItem) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Shared Text", viewModel.clipboardItem.value.url)
        clipboard.setPrimaryClip(clip)
        viewModel.sendIntent(ClipboardSaveIntent.postClipboarInsertIntent(PrefManager.folderIdList[0],ClipboardItem))



    }
}
