package com.polaris.clipboard

import android.app.Activity
import kotlinx.coroutines.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSNegativeButton
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.designsystem.ui.theme.CDSTransparentButton
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.designsystem.ui.theme.PrimaryColor
import com.polaris.designsystem.ui.theme.textColorGray
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ClipboardActivity : AppCompatActivity() {
    private val viewModel: ClipboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {
            viewModel.siteInformation(url = sharedText)
//            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("Shared Text", sharedText)
//            clipboard.setPrimaryClip(clip)
//
//            viewModel.processIntent(ClipboardIntent.postClipboarInsertIntent(sharedText))
//            Toast.makeText(this, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()

        }

        setContent {

            ClipboardView(onClick = {

                if (!sharedText.isNullOrEmpty()) {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Shared Text", sharedText)
                    clipboard.setPrimaryClip(clip)

                    viewModel.processIntent(ClipboardIntent.postClipboarInsertIntent(sharedText))
                    Toast.makeText(this, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()

                }
            }, onDismiss = {

                finish()


            })

        }
    }
}

@Composable
fun ClipboardView(
    viewModel: ClipboardViewModel = hiltViewModel(),
    onClick: () -> Unit = {},
    onDismiss: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalActivity.current
    val clipboardItem = viewModel.clipboardItem.collectAsState()


    when (uiState) {
        is ClipboardUiState.Initialize -> {
            ClipboardSaveView(
                title = clipboardItem.value.title,
                siteName = clipboardItem.value.type,
                isAnimation = true,
                onDismiss = {
                    onDismiss()

                },
                onConfirm = {

                    onClick()

                })
        }

        is ClipboardUiState.ClipboardSave -> {
            LottieAnimationAndExit(activity)
        }
    }

}


@Composable
fun LottieAnimationAndExit(activity: Activity?) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lottie))
    val progress by animateLottieCompositionAsState(composition)


    // 애니메이션 종료 감지 후 앱 종료
    LaunchedEffect(progress) {
        if (progress == 1f) {
            activity?.finish()  // Activity 종료
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
@Preview
fun ClipboardSaveView(
    title: String = "제목",
    siteName: String = "사이트 제목",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    isAnimation: Boolean = false
) {
    var isVisible by remember { mutableStateOf(isAnimation) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {

        if (isAnimation) isVisible = false
    }

    ClippyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x3B363636))
                .clickable {
                    isVisible = !isVisible
                    coroutineScope.launch {
                        delay(300) // 2초 대기
                        onDismiss()
                    }

                },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = !isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it * 2 }, // 화면 아래에서 올라옴
                    animationSpec = tween(durationMillis = 1200, easing = EaseInOutCubic)
                ) + fadeIn(animationSpec = tween(1200)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500, easing = EaseInOutCubic)
                ) + fadeOut(animationSpec = tween(500))
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .navigationBarsPadding(),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            text = "클리피가 링크를 저장할께요!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "링크 제목",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColorGray
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "사이트 제목",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColorGray,
                        )
                        Text(
                            text = siteName,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        CDSButton(buttonText = "저장 하기", onClick = {
                            onConfirm()
                            onDismiss()
                        })
                        Spacer(modifier = Modifier.height(12.dp))
                        CDSTransparentButton(buttonText = "편집", onClick = onDismiss)
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ClipboardSaveViewTest(
    title: String = "",
    siteName: String = "",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {

    var url by remember { mutableStateOf(TextFieldValue(title)) }
    var siteNameState by remember { mutableStateOf(TextFieldValue(siteName)) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            Text(text = "클리피가 링크를 잘 저장할께요!")

            CDSTextField(
                label = "URL",
                value = url.text,
                onValueChange = { url = url.copy(text = it) },
                onFocusChange = {
                    Log.e("FocusChanged", "URL 입력란에 포커스됨")
                    //  viewModel.expandView()  // ✅ ViewModel의 상태를 변경
                }
            )

            CDSTextField(
                label = "사이트 이름",
                value = siteNameState.text,
                onValueChange = { siteNameState = siteNameState.copy(text = it) }
            )

            CDSButton(buttonText = "저장하기") {
                // viewModel.collapseView() // ✅ 저장 버튼 누르면 축소되도록 설정
            }
        }
    }
}
