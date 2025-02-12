package com.polaris.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.polaris.clipboard_list.ClipboardListSeen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.processIntent(MainIntent.getAllClipboardListIntent)
            MainScreen(viewModel)
        }
    }

}

@Composable
fun MainScreen(viewModel: MainViewModel) {

    ClipboardListSeen(viewModel.clipboardDataList.collectAsState().value)


}
