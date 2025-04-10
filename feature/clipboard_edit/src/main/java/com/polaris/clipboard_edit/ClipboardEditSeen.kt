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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.polaris.clipboard_edit.intent.ClipboardEditIntent
import com.polaris.clipboard_edit.state.ClipboardEditState
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSSegmentedButtons
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.designsystem.ui.theme.textColorGray
import com.polaris.model.model.ClipboardItem
import com.polaris.model.model.FolderInfo

@Composable
fun ClipboardEditSeen(
    clipboardFolder: ClipboardItem,
    onSaveSuccess: () -> Unit
) {
    ClippyTheme {
        val viewModel: ClipboardEditViewModel = hiltViewModel()
        val clipboardItem = viewModel.clipboardItem.collectAsState()
        val folderNameList = viewModel.folderNameList.collectAsState()
        val uiState = viewModel.uiState.collectAsState()

        when (uiState.value) {
            ClipboardEditState.Initialize -> {
                if (!viewModel.isInitialized){
                    viewModel.isInitialized = true
                    viewModel.updateUiState(ClipboardEditState.Loading)
                }

            }
            ClipboardEditState.Loading->{
                viewModel.updateClipboardItem(clipboardFolder)
                viewModel.sendIntent(ClipboardEditIntent.getLocalClipboardFolderName)
            }


            ClipboardEditState.ClipboardSaveSuccess -> {
                onSaveSuccess()
                viewModel.updateUiState(ClipboardEditState.Initialize)
            }
        }

        ClipboardEditView(
            clipboardItem = clipboardItem.value,
            nameList = folderNameList.value,
            onClipUpdateSaveClick = { type, title -> viewModel.updateClipboardItem(type, title) },
            onSaveClick = {
                viewModel.sendIntent(
                    ClipboardEditIntent.postClipboarInsertIntent(
                        folderId = it.id,
                        clipboardItem.value
                    )
                )
            }

        )
    }

}

@Composable
@Preview(showBackground = true)
fun ClipboardEditView(
    clipboardItem: ClipboardItem = ClipboardItem(),
    nameList: List<FolderInfo> = listOf(),
    onClipUpdateSaveClick: (type: String, title: String) -> Unit = { _, _ -> },
    onSaveClick: (FolderInfo) -> Unit = {}
) {
    var title by remember(clipboardItem) { mutableStateOf(clipboardItem.title) }
    var type by remember(clipboardItem) { mutableStateOf(clipboardItem.type) }
    var selectedIndex by remember { mutableStateOf(0) }
    var folder = FolderInfo()
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
            CDSTextField(
                label = stringResource(R.string.site_explanation),
                value = type,
                onValueChange = { type = it })
            Spacer(modifier = Modifier.height(8.dp))
            CDSTextField(
                label = stringResource(R.string.site_title),
                value = title,
                onValueChange = { title = it })
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.select_folder),
                style = MaterialTheme.typography.bodySmall,
                color = textColorGray,
            )
            Spacer(modifier = Modifier.height(4.dp))
            CDSSegmentedButtons(nameList.map { it.name }, selectedIndex, {
                selectedIndex = it
                folder = nameList[it]
            })
        }

        Spacer(modifier = Modifier.weight(1f))
        CDSButton(buttonText = stringResource(R.string.clipboard_data_save),
            onClick = {
                onClipUpdateSaveClick(type, title)
                onSaveClick(folder)
            }

        )
    }

}
