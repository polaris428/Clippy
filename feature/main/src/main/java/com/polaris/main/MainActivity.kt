package com.polaris.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.polaris.clipboard.navigation.ClipboardRoute
import com.polaris.clipboard.navigation.clipboardNavGraph
import com.polaris.clipboard_edit.navigation.clipboardEdit
import com.polaris.clipboard_edit.navigation.navigateClipboardEdit
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.clipboard_list.navigation.ClipboardList
import com.polaris.clipboard_list.navigation.clipboardListNavGraph
import com.polaris.clipboard_list.navigation.navigateClipboardList
import com.polaris.data.local.ClipboardItem
import com.polaris.shared.MainViewModel
import com.polaris.shared.intent.MainIntent
import com.polaris.util.GoogleSignInHelper
import com.polaris.util.PrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private val viewModel: MainViewModel by viewModels()
    lateinit var  navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInHelper = GoogleSignInHelper(
            activity = this,
            onSignInSuccess = { task->
//                PrefManager.userUid=task.result.user!!.uid
//                PrefManager.userName=task.result.user!!.displayName.toString()


            },
            onSignInFailure = { exception ->

                // viewModel.onGoogleSignInFailure(exception)
            }
        )
        viewModel.processIntent(MainIntent.getAllClipboardListIntent)
        setContent {

             navController = rememberNavController()


            NavHost(navController = navController, startDestination = ClipboardList.route) {

                clipboardListNavGraph(viewModel.clipboardDataList, onEditClick = { item: ClipboardItem ->
                    viewModel.updateClipboardItem(item)
                    navController.navigateClipboardEdit()

                })

                clipboardEdit(mainViewModel = viewModel, onSaveClick = { type, title ->
                    updateClipDate(type,title)
                    saveClipboard()
                })
            }


        }
    }

    fun updateClipDate(type:String , title:String){
        viewModel.updateClipboardItem(type,title)
    }


    fun saveClipboard() {


        viewModel.processIntent(MainIntent.updateClipboarIntent)

        navController.navigateClipboardList()

    }

}

@Composable
fun MainScreen(viewModel: MainViewModel) {




}
