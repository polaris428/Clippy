package com.polaris.clipboard_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.polaris.data.local.ClipboardItem

@Composable
fun ClipboardListSeen(clipboardItem: List<ClipboardItem>?) {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(20.dp)
    ) {
        Header()
        LazyColumn() {
            if (!clipboardItem.isNullOrEmpty()) {
                items(clipboardItem) { item ->
                    Log.e("polaris4280", item.title)
                    Log.e("polaris4280", item.url.toString())
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

    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth(1f)
    ) {
        if (!clipboardItem.faviconUrl.isNullOrEmpty()) {
            displayImage(clipboardItem.faviconUrl!!)
        }

        Column(
            modifier = Modifier.padding(start = 10.dp)

        ) {
            Text(
                text = clipboardItem.title, fontSize = 15.sp, maxLines = 1, // 한 줄로 고정
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (!clipboardItem.url.isNullOrEmpty()) {

                    Text(
                        text = clipboardItem.type,
                        color = Color(
                            0xFF808080
                        )
                    )
                }

            }


        }


    }


}

@Preview
@Composable
fun Header() {
    Row {
        Text("Clippy", fontSize = 35.sp)
        //Icon()
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
            .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp)) // 테두리 추가

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