package com.polaris.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.CLIPBOARD_SERVICE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import com.polaris.clipboard.state.ClipboardState
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSSegmentedButtons
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.designsystem.ui.theme.CDSTransparentButton
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.designsystem.ui.theme.textColorGray
import com.polaris.model.model.ClipboardItem
import com.polaris.model.model.FolderInfo
import com.polaris.util.PrefManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.polaris.designsystem.R

@Composable
fun ClipboardSeen(
    url: String,
    onSaveSuccess: () -> Unit = {},
    onEditClick: (item: ClipboardItem) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val viewModel: ClipboardViewModel = hiltViewModel()

    val clipboardItem = viewModel.clipboardItem.collectAsState()
    val folderNameList = viewModel.folderNameList.collectAsState()
    val uiState = viewModel.uiState.collectAsState()

    when (uiState.value) {
        ClipboardState.Initialize -> {

            viewModel.sendIntent(ClipboardIntent.getLocalClipboardFolderName)
        }

        ClipboardState.SiteCrawlingStart -> {
            viewModel.sendIntent(ClipboardIntent.getUrlCrawlingInfo(url))
        }

        ClipboardState.SiteCrawlingComplete -> {

        }

        ClipboardState.ClipboardSaveSuccess -> {
            onSaveSuccess()
        }
    }
    ClipboardView(
        clipboardItem.value,
        nameList = folderNameList.value,
        onSaveClick = {
            viewModel.sendIntent(ClipboardIntent.postClipboarInsertIntent(it))
        },
        onEditClick = onEditClick,
        onDismiss = onDismiss
    )
}

@Composable
fun ClipboardView(
    clipboardItem: ClipboardItem,
    nameList: List<FolderInfo>,
    onSaveClick: (FolderInfo) -> Unit,
    onEditClick: (item: ClipboardItem) -> Unit,
    onDismiss: () -> Unit
) {


    ClipboardSaveView(
        clipboardItem,
        nameList,
        isAnimation = true,
        onDismiss = {

            onDismiss()

        },
        onConfirm = {
            onSaveClick(it)


        },
        onEditClick = onEditClick
    )


}


@Composable
@Preview
fun ClipboardSaveView(
    clipboardItem: ClipboardItem = ClipboardItem(),
    nameList: List<FolderInfo> = listOf(),
    onDismiss: () -> Unit = {},
    onConfirm: (FolderInfo) -> Unit = {},
    onEditClick: (item: ClipboardItem) -> Unit = {},
    isAnimation: Boolean = false
) {

    var isVisible by remember { mutableStateOf(isAnimation) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) }
    val title = if (clipboardItem.title.isBlank()) stringResource(R.string.site_crawling_message) else clipboardItem.title
    val siteName = if (clipboardItem.type.isBlank())  stringResource(R.string.site_crawling_message)  else clipboardItem.type
    var folder = FolderInfo()
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
                            text = stringResource(R.string.clipboard_data_save_message),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.link_title),
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
                            text = stringResource(R.string.site_title),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColorGray,
                        )
                        Text(
                            text = siteName,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.select_folder),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColorGray,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CDSSegmentedButtons(nameList.map { it.name }, selectedIndex, {
                            selectedIndex = it
                            folder =  nameList[it]
                        })
                        Spacer(modifier = Modifier.height(32.dp))
                        CDSButton(buttonText = stringResource(R.string.clipboard_data_save), onClick = {
                            saveClipboard(context = context, clipboardItem = clipboardItem)
                            onConfirm(folder)

                        })
                        Spacer(modifier = Modifier.height(12.dp))
                        CDSTransparentButton(
                            buttonText = stringResource(R.string.clipboard_data_edit),
                            onClick = { onEditClick(clipboardItem) })
                    }
                }
            }
        }
    }
}


fun saveClipboard(context: Context, clipboardItem: ClipboardItem) {
    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Shared Text", clipboardItem.url)
    clipboard.setPrimaryClip(clip)


}