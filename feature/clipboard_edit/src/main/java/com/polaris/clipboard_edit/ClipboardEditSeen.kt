package com.polaris.clipboard_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSTextField


@Composable
fun ClipboardEditSeen() {
}

@Composable
@Preview(showBackground = true)
fun ClipboardEditView() {
    Column(
        Modifier
            .fillMaxSize()
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
                CDSTextField()
                Spacer(modifier = Modifier.height(8.dp))
                CDSTextField()
            }
        }
        CDSButton() // 자동으로 아래에 정렬됨
    }
}