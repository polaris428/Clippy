package com.polaris.folder_join

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
import com.polaris.folder_join.intent.FolderJoinIntent
import com.polaris.folder_join.state.FolderJoinState
import com.polaris.model.model.ClipboardFolder
import com.polaris.util.PrefManager

@Composable
fun FolderJoinSeen(onPostFolderSuccess:()->Unit,onPostFolderFile:()->Unit) {
    val viewModel : FolderJoinInViewModel  = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    when(uiState.value){
        is FolderJoinState.Initialize -> {

        }

        is FolderJoinState.PostFolderLoading -> {

        }
        is FolderJoinState.PostFolderSuccess -> {
            onPostFolderSuccess()
        }

        is FolderJoinState.PostFolderFail -> {

        }
    }

    FolderJoinView(){
        viewModel.sendIntent(FolderJoinIntent.postFolderJoinIntent(it))
    }
}

@Composable
@Preview(showBackground = true)
fun FolderJoinView(uiState: FolderJoinState= FolderJoinState.Initialize,onSaveClick: (String) -> Unit = {}) {

    var name by remember { mutableStateOf("") }
    CDSColumn {

    
        Spacer(modifier = Modifier.height(16.dp)) // 빈 공간을 최대한 차지하도록 설정
        Text("초대 코드를 입력해주세요", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp)) // 빈 공간을 최대한 차지하도록 설정
        CDSTextField(label = "폴더 이름", value = name, onValueChange = { name = it })
        ErrorTextView(uiState)

        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 최대한 차지하도록 설정
        CDSButton(buttonText = "추가하기", onClick = { onSaveClick(name) })
    }

}
@Composable
fun ErrorTextView(uiState: FolderJoinState){
    if (uiState is FolderJoinState.PostFolderFail){
        var errormMssage =""
        when(uiState.errorCode){
            501 ->{
                errormMssage = "폴더 추가에 실패했어요"
            }
            502 ->{
                errormMssage ="이미 참여하고 있는 폴더입니다."
            }
            503 ->{

            }

        }

        Text(text = errormMssage)
    }

}
