package com.polaris.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.util.fetchWebTitle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//
//class ClipboardActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
//
//        if (!sharedText.isNullOrEmpty()) {
//            // 클립보드에 저장
//            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("Shared Text", sharedText)
//            clipboard.setPrimaryClip(clip)
//
//            GlobalScope.launch {
//                Log.e("polaris42800", sharedText)
//
//
//                Log.e("polaris428", fetchWebTitle(sharedText).toString())
//            }
//
//        }
//
//        Toast.makeText(this, "텍스트가 클립보드에 복사되었습니다!", Toast.LENGTH_SHORT).show()
//        finish() // 자동으로 닫기
//        setContent {
//
//            ClippyTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    com.polaris.designsystem.ui.theme.ClippyTheme {
//        Greeting("Android")
//    }
//}