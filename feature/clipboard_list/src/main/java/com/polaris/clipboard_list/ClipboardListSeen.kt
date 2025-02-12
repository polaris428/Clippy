package com.polaris.clipboard_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.polaris.data.local.ClipboardItem

@Composable
fun ClipboardListSeen (clipboardItem : List<ClipboardItem>?){
    Column {
        LazyColumn() {
            if (!clipboardItem.isNullOrEmpty()){
                items(clipboardItem){ item ->
                    Log.e("polaris4280",item.title)
                    Text(text = item.title)
                }
            }


        }

    }
}
val dummyData = ClipboardItem(
    id = 3,
    type = "url",
    url = "https://www.github.com",
    title = "GitHub",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)

    @Preview
@Composable
fun preView(){
    ClipboardItemView(dummyData)
}

@Composable
fun ClipboardItemView(clipboardItem: ClipboardItem){
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = clipboardItem.title)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!clipboardItem.faviconUrl.isNullOrEmpty()){
                displayImage(clipboardItem.faviconUrl!!)
            }

            if (!clipboardItem.url.isNullOrEmpty()){
                Text(text = clipboardItem.url!!)
            }

        }

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

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(12.dp)
            .clip(RoundedCornerShape(4.dp))
    )
}
