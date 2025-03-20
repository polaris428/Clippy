package com.polaris.folder_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.CDSSwitch
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.folder_edit.intent.FolderEditIntent
import com.polaris.folder_edit.state.FolderEditState
import com.polaris.model.model.ClipboardFolder
import com.polaris.util.PrefManager

@Composable
fun FolderEditSeen(onPostFolderSuccess:()->Unit,onPostFolderFile:()->Unit) {
    val viewModel : FolderEditViewModel  = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    when(uiState.value){
        FolderEditState.Initialize -> {

        }

        FolderEditState.PostFolderLoading -> {

        }
        FolderEditState.PostFolderSuccess -> {
            onPostFolderSuccess()
        }
        FolderEditState.PostFolderFail -> {
            onPostFolderFile()
        }
    }

    FolderEditView(){
        viewModel.sendIntent(FolderEditIntent.postFolderIntent(it))
    }
}

@Composable
@Preview(showBackground = true)
fun FolderEditView(onSaveClick: (ClipboardFolder) -> Unit = {}) {
    var isChecked by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    CDSColumn {

    
        Spacer(modifier = Modifier.height(16.dp)) // 빈 공간을 최대한 차지하도록 설정
        Text("폴더의 이름을 입력해주세요", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp)) // 빈 공간을 최대한 차지하도록 설정
        CDSTextField(label = "폴더 이름", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(1f),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween ) {
            Text("다른 사람들과 공유", style = MaterialTheme.typography.bodyMedium)
            CDSSwitch(isChecked = isChecked , onCheckedChange = { isChecked = it})
        }

        Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 최대한 차지하도록 설정
        CDSButton(buttonText = "추가하기", onClick = { onSaveClick(ClipboardFolder(name = name, owner = PrefManager.userUid, isShare = isChecked)) })
    }

}

