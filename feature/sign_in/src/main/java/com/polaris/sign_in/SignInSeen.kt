package com.polaris.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.CDSTextField
import com.polaris.designsystem.ui.theme.Gray40

@Composable
fun SignInSeen() {

}

@Composable
@Preview(showBackground = true)
fun SignInView(onSignInClick:()->Unit ={}) {
    CDSColumn(horizontalAlignment = Alignment.CenterHorizontally){
        Row(Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Clippy",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    "복사 그 이상, 더 스마트한 클립보드.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )

            }
        }
        Text(
            "로그인 없이 계속하기",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray40
        )
        Spacer(modifier = Modifier.height(8.dp))
        CDSButton(buttonText = "로그인 하기", onClick = { onSignInClick() })
    }
}