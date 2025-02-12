package com.polaris.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.polaris.clipboard.ClipboardSeen
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.data.local.ClipboardItem
import com.polaris.main.ui.theme.ClippyTheme
import com.polaris.util.fetchWebTitle
import com.polaris.util.getGoogleFaviconUrl
import com.polaris.util.isUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        setContent {
            MainScreen(viewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent) // ✅ 앱이 실행 중일 때 공유 Intent 처리
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_MAIN -> {
                Log.e("polaris428", "main")
                viewModel.processIntent(MainIntent.getAllClipboardListIntent)
                viewModel.getAllClipboardList()
            }

            Intent.ACTION_SEND -> {
                Log.e("polaris428", "send")
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (!sharedText.isNullOrEmpty()) {
                    handleSharedContent(sharedText)
                }
                finish()
            }

            else -> {
                Log.e("polaris428", "dddd")
                Log.d("MainActivity", "Unknown Intent action: ${intent?.action}")
            }
        }
    }

    private fun handleSharedContent(sharedText: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Shared Text", sharedText)
        clipboard.setPrimaryClip(clip)



        viewModel.processIntent(MainIntent.postClipboarInsertIntent(sharedText))
        showSaveAnimation()
    }

    private fun showSaveAnimation() {
        Log.d("Animation", "저장 애니메이션 실행")
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val clipboardDataList by viewModel.clipboardDataList.collectAsState()
    when (state) {
        is MainUiState.ClipboardList -> ClipboardListSeen(clipboardDataList)
        is MainUiState.ClipboardSaved -> ClipboardSeen()
        else -> Text("로딩 중...")
    }


}
