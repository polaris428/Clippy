package com.polaris.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.regex.Pattern

suspend fun fetchWebTitle(url: String): String? {
    return withContext(Dispatchers.IO) { // 네트워크 작업은 IO 스레드에서 실행
        try {
            val doc = Jsoup.connect(url).get() // HTML 문서 가져오기
            doc.title() // <title> 태그 값 반환
        } catch (e: Exception) {
            e.printStackTrace()
            null // 실패 시 null 반환
        }
    }
}
private val urlPattern = Pattern.compile(
    "^(https?://)?" +
            "(([\\da-z.-]+)\\.([a-z.]{2,6})" +
            "|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
            "(:[0-9]{1,5})?" +
            "(/[^\\s]*)?$",
    Pattern.CASE_INSENSITIVE
)

fun isUrl(text: String): Boolean {
    return urlPattern.matcher(text).matches()
}
fun getGoogleFaviconUrl(url: String): String {
    return "https://www.google.com/s2/favicons?sz=64&domain_url=$url"
}