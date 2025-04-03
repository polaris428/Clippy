package com.polaris.clipboard_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.model.model.ClipboardItem

@Composable
fun ClipboardEditSeen(clipboardFolder: ClipboardItem, onSaveClick: (type: String, title: String) -> Unit = { _, _ -> }) {
    ClippyTheme {
        ClipboardEditView(
            clipboardItem = clipboardFolder,
            onSaveClick = { type, title -> onSaveClick(type, title) }
        )
    }

}

@Composable
@Preview(showBackground = true)
fun ClipboardEditView(
    clipboardItem: ClipboardItem =ClipboardItem(),
    onSaveClick: (type: String, title: String) -> Unit = { _, _ -> }
) {
    var title by remember(clipboardItem) { mutableStateOf(clipboardItem.title) }
    var type by remember(clipboardItem) { mutableStateOf(clipboardItem.type) }

    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .systemBarsPadding()

            .padding(20.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            CDSTextField(label = stringResource(R.string.site_explanation), value = type, onValueChange = { type = it })
            Spacer(modifier = Modifier.height(8.dp))
            CDSTextField(label = stringResource(R.string.site_title), value = title, onValueChange = { title = it })
        }
        Spacer(modifier = Modifier.weight(1f))
        CDSButton(buttonText = stringResource(R.string.clipboard_data_save), onClick = { onSaveClick(type, title) })
    }
}
