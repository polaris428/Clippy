package com.polaris.clipboard_list

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.polaris.data.local.ClipboardItem
import com.polaris.designsystem.ui.theme.Gray20
import com.polaris.designsystem.ui.theme.Gray50
import com.polaris.designsystem.ui.theme.Gray60
import com.polaris.designsystem.ui.theme.Gray80
import com.polaris.designsystem.ui.theme.LineView
import com.polaris.util.convertTimestampToMonthDay

@Composable
fun ClipboardListSeen(clipboardItem: List<ClipboardItem>?) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Header()
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), // 모서리 둥글게
            //elevation = CardDefaults.cardElevation(0.5.dp), // 그림자 효과
            colors = CardDefaults.cardColors(containerColor = Color.White) // 배경색을 하얀색으로 설정

        ) {
            if (!clipboardItem.isNullOrEmpty()) {
                clipboardItem.forEach  { item ->
                    ClipboardItemView(item)
                }
            }


        }


    }
}

val dummyData = ClipboardItem(
    id = 3,
    type = "GitHub",
    url = "https://www.github.com",
    title = "안드로이드 라이브러리 모음",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)

@Preview(showBackground = true)
@Composable
fun preView() {
    ClipboardItemView(dummyData)
}

@Composable
fun ClipboardItemView(clipboardItem: ClipboardItem) {
    Modifier.fillMaxWidth()
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .clickable {
                if (!clipboardItem.url.isNullOrBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clipboardItem.url))
                    intent.setPackage("com.android.chrome") // Chrome에서 열도록 설정

                    context.startActivity(intent)
                }

            }
            .padding(top = 14.dp , start = 8.dp)
    ) {
        if (!clipboardItem.faviconUrl.isNullOrEmpty()) {
            displayImage(clipboardItem.faviconUrl!!)
        }

        Column(
            modifier = Modifier.padding(start = 10.dp)

        ) {
            Text(
                text = clipboardItem.title, fontSize = 16.sp, maxLines = 1, // 한 줄로 고정
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (!clipboardItem.url.isNullOrEmpty()) {

                    Text(
                        text = convertTimestampToMonthDay(clipboardItem.timestamp),
                        color = Gray50,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " | ",
                        color =  Gray50
                        ,
                        fontSize = 14.sp
                    )
                    Text(
                        text = clipboardItem.type,
                        color =  Gray50
                        ,
                        fontSize = 12.sp
                    )


                }

            }

            LineView()
        }

    }




}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Clippy",
            fontSize = 35.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF333333)
        )
    }
}


@Preview
@Composable
fun displayImage(imageUrl: String = "") {
    val painter = if (LocalInspectionMode.current) {
        // 프리뷰 모드에서는 Image와 painterResource 사용
        painterResource(id = R.drawable.logo)
    } else {
        // 실제 모드에서는 rememberAsyncImagePainter 사용
        rememberAsyncImagePainter(model = imageUrl)
    }
    Surface(
        shape = RoundedCornerShape(8.dp), // 둥근 모서리 적용
        tonalElevation = 4.dp, // 그림자 효과 추가
        color = Color.White,
        modifier = Modifier
            .size(48.dp)


    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewTest() {
    Column {

        ClipboardListSeen(listOf(dummyData, dummyData, dummyData, dummyData, dummyData))

    }


}