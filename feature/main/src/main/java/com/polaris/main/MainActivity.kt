package com.polaris.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard.navigation.ClipboardRoute
import com.polaris.clipboard.navigation.clipboardNavGraph
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.clipboard_list.navigation.ClipboardList
import com.polaris.clipboard_list.navigation.clipboardListNavGraph
import com.polaris.shared.MainViewModel
import com.polaris.shared.intent.MainIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    var startDestination = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText.isNullOrEmpty()) {
            viewModel.processIntent(MainIntent.getAllClipboardListIntent)
            startDestination = ClipboardList.route

        } else {
            viewModel.siteInformation(url = sharedText)
            startDestination = ClipboardRoute.route
        }
        setContent {

            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = startDestination) {
                clipboardNavGraph(
                    mainViewModel = viewModel,
                    onSaveClick = {
                    saveClipboard()

                }, onEditClick = {
                    navController.navigateClipboardEdit()
                }, onDismiss = {
                    finish()
                })
                clipboardEdit(mainViewModel = viewModel, onSaveClick = { saveClipboard() })
                clipboardListNavGraph(viewModel.clipboardDataList)
            }

           // MainScreen(viewModel)
        }
    }

    fun saveClipboard() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Shared Text", viewModel.clipboardItem.value.url)
        clipboard.setPrimaryClip(clip)

        viewModel.processIntent(MainIntent.postClipboarInsertIntent)
        Toast.makeText(this@MainActivity, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()
    }

}

@Composable
fun MainScreen(viewModel: MainViewModel) {

    ClipboardListSeen(viewModel.clipboardDataList.collectAsState().value)


}
