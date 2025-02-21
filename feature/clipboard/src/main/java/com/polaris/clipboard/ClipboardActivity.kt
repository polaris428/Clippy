package com.polaris.clipboard

import android.app.Activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.polaris.clipboard.intent.ClipboardIntent
import com.polaris.clipboard.state.ClipboardUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class ClipboardActivity : AppCompatActivity() {
    private val viewModel: ClipboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Shared Text", sharedText)
            clipboard.setPrimaryClip(clip)

            viewModel.processIntent(ClipboardIntent.postClipboarInsertIntent(sharedText))
            Toast.makeText(this, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()

        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            when(uiState){
                is ClipboardUiState.Initialize ->{
                    ClipboardSaveDialog(onDismiss = {} , onConfirm = {
                        s1 ,s2 ->
                    })
                }
                is ClipboardUiState.ClipboardSave ->{
                    LottieAnimationAndExit(this)
                }
            }


        }
    }
}




@Composable
fun LottieAnimationAndExit(activity: Activity) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lottie))
    val progress by animateLottieCompositionAsState(composition)


    // 애니메이션 종료 감지 후 앱 종료
    LaunchedEffect(progress) {
        if (progress == 1f) {
            activity.finish()  // Activity 종료
            exitProcess(0)  // 프로세스 종료
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(100.dp)
        )
    }

}

@Composable
@Preview(showBackground = true)
fun ClipboardSaveDialog(title:String ="",siteName:String ="",onDismiss: () -> Unit={}, onConfirm: (String, String) -> Unit = { _, _ -> }){

    var text1 by remember { mutableStateOf(TextFieldValue()) }
    var text2 by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("입력 다이얼로그", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("첫 번째 입력") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = { Text("두 번째 입력") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm(text1.text, text2.text) }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}