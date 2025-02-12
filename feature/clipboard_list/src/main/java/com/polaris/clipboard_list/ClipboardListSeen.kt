package com.polaris.clipboard_list

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.lifecycle.LiveData
import com.polaris.data.local.ClipboardItem

@Composable
fun ClipboardListSeen (clipboardItem : List<ClipboardItem>?){
    Column {
        LazyColumn() {
            if (!clipboardItem.isNullOrEmpty()){
                items(clipboardItem){ item ->
                    Log.e("polaris4280",item.title.toString())
                    Text(text = item.title)
                }
            }


        }

    }
}