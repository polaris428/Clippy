package com.polaris.clipboard_edit

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
import com.polaris.data.local.ClipboardItem
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.shared.MainViewModel

@Composable
fun ClipboardEditSeen(viewModel: MainViewModel, onSaveClick: (type: String, title: String) -> Unit = { _, _ -> }) {
    ClipboardEditView(
        clipboardItem = viewModel.clipboardItem.collectAsState().value,
        onSaveClick = { type, title -> onSaveClick(type, title) }
    )
}

@Composable
@Preview(showBackground = true)
fun ClipboardEditView(
    clipboardItem: ClipboardItem = ClipboardItem(),
    onSaveClick: (type: String, title: String) -> Unit = { _, _ -> }
) {
    var title by remember(clipboardItem) { mutableStateOf(clipboardItem.title) }
    var type by remember(clipboardItem) { mutableStateOf(clipboardItem.type) }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()

            .padding(20.dp)
    ) {
        Row(Modifier.weight(1f)) {
            Column(Modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                CDSTextField(label = "사이트 설명", value = type, onValueChange = { type = it })
                Spacer(modifier = Modifier.height(8.dp))
                CDSTextField(label = "사이트 제목", value = title, onValueChange = { title = it })
            }
        }
        CDSButton(buttonText = "저장하기", onClick = { onSaveClick(type, title) })
    }
}
