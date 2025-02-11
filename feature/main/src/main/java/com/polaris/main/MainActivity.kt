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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

        setContent {

        }
        handleIntent(intent)


        //  finish() // 화면을 닫고 백그라운드로 실행되도록 처리
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent) // ✅ 앱이 실행 중일 때 공유 Intent 처리
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_MAIN -> {
                Log.e("polaris428", "main")
                // ✅ 일반 실행: 기본 UI 표시
                showNormalUI()
            }

            Intent.ACTION_SEND -> {
                Log.e("polaris428", "send")
                // ✅ 공유 실행: 클립보드 저장 UI 표시
                handleSharedContent(intent)
            }

            else -> {
                // ✅ 기타 액션 (예외 처리)
                Log.e("polaris428", "dddd")
                Log.d("MainActivity", "Unknown Intent action: ${intent?.action}")
            }
        }
    }

    private fun showNormalUI() {
        // 기본 UI 로직
        Log.d("MainActivity", "앱을 일반 실행함")
        viewModel.getAllClipboardList()
    }

    private fun handleSharedContent(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {

            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Shared Text", sharedText)
            clipboard.setPrimaryClip(clip)
            // ✅ 공유된 텍스트를 처리
            if (isUrl(sharedText)) {
                saveWebLinkToClipboard(sharedText)
            } else {
                saveToClipboard(sharedText)
            }

            showSaveAnimation()
        }
    }

    private fun saveToClipboard(text: String) {
        // 클립보드에 저장하는 로직

        if (!text.isNullOrEmpty()) {
            // 클립보드에 저장


            viewModel.postClipboardInsert(
                ClipboardItem(
                    type = "web",
                    url = null,
                    title = text,
                    faviconUrl = null
                )
            )


            finish()
        }
    }

    private fun saveWebLinkToClipboard(text: String) {
        runBlocking {

            val webTitle = fetchWebTitle(text).toString()
            val imageUrl = getGoogleFaviconUrl(text)
            viewModel.postClipboardInsert(
                ClipboardItem(
                    type = "web",
                    url = text,
                    title = webTitle,
                    faviconUrl = imageUrl
                )
            )

        }
    }

    private fun showSaveAnimation() {
        // ✅ 저장 애니메이션을 표시 (ex: Lottie 애니메이션)
        Log.d("Animation", "저장 애니메이션 실행")
    }


}