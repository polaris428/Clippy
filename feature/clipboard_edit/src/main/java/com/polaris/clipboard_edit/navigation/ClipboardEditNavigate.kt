package com.polaris.clipboard_edit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.polaris.clipboard_edit.ClipboardEditSeen
import com.polaris.model.model.ClipboardItem
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigateClipboardEdit(item: ClipboardItem) {
    val json = Gson().toJson(item)
    val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())


    navigate(ClipboardEditRoute.createRoute(encoded))
}

fun NavGraphBuilder.clipboardEdit(
    onSaveClick: (type: String, title: String) -> Unit = { _, _ -> },
) {
    composable(route = ClipboardEditRoute.routeWithArgs) { backStackEntry ->
        val clipboardItem = rememberClipboardItem(backStackEntry)
        if (clipboardItem != null) {
            ClipboardEditSeen(clipboardItem, onSaveClick = onSaveClick)

        }
    }
}
object ClipboardEditRoute {
    const val route = "clipboard_edit"
    const val routeWithArgs = "$route?itemJson={itemJson}"

    fun createRoute(itemJson: String): String {
        return "$route?itemJson=$itemJson"
    }
}


@Composable
fun rememberClipboardItem(backStackEntry: NavBackStackEntry): ClipboardItem? {
    val jsonEncoded = backStackEntry.arguments?.getString("itemJson") ?: return null
    val json = URLDecoder.decode(jsonEncoded, StandardCharsets.UTF_8.toString())

    return try {
        Gson().fromJson(json, ClipboardItem::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}