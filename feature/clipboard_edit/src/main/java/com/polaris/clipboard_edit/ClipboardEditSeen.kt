package com.polaris.clipboard_edit

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polaris.clipboard_edit.intent.ClipboardEditIntent
import com.polaris.data.local.ClipboardItem
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSTextField


@Composable
fun ClipboardEditSeen(clipboardItem: ClipboardItem) {
    val viewModel: ClipboardEditViewModel = hiltViewModel()
    viewModel.updateClipboardItem(clipboardItem)
    val viewModelClipboardItem = viewModel.clipboardItem.collectAsState()
    ClipboardEditView(clipboardItem = viewModelClipboardItem.value) {
        viewModel.processIntent(ClipboardEditIntent.postClipboarInsertIntent(viewModel.clipboardItem.value.url.toString()))
    }
}

@Composable
@Preview(showBackground = true)
fun ClipboardEditView(clipboardItem: ClipboardItem = ClipboardItem(), onClick: () -> Unit = {}) {
    var title by remember { mutableStateOf(clipboardItem.title) }
    var type by remember { mutableStateOf(clipboardItem.type) }
    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(Modifier.weight(1f)) { // Row가 가능한 모든 공간을 차지하도록 설정
            Column {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                CDSTextField(label = "사이트 설명" , value = type, onValueChange = {type = it})
                Spacer(modifier = Modifier.height(8.dp))
                CDSTextField(label = "사이트 제목", value = title, onValueChange = { title = it })
            }
        }
        CDSButton(buttonText = "저장하기", onClick = { onClick() })
    }
}