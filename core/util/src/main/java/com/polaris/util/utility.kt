package com.polaris.util

import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.regex.Pattern
fun Any.toJson(): String {
    return "\n" + GsonBuilder().setPrettyPrinting().create().toJson(this)
}
suspend fun fetchWebTitle(url: String): String {
    return withContext(Dispatchers.IO) { // 네트워크 작업은 IO 스레드에서 실행
        try {
            val doc = Jsoup.connect(url).get() // HTML 문서 가져오기
            doc.title() // <title> 태그 값 반환
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
private val urlPattern = Pattern.compile(
    "(https?://)?" +  // http:// 또는 https:// (선택적)
            "(([\\da-z.-]+)\\.([a-z.]{2,6})" + // 도메인 이름 (예: example.com)
            "|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" + // 또는 IPv4 주소
            "(:[0-9]{1,5})?" + // 포트 번호 (선택적)
            "(/[^\\s]*)?", // 경로 (선택적)
    Pattern.CASE_INSENSITIVE
)

fun isUrl(text: String): Boolean {
    return urlPattern.matcher(text).find() // 부분적으로 URL 포함 여부 확인
}
fun getGoogleFaviconUrl(url: String): String {
    return "https://www.google.com/s2/favicons?sz=64&domain_url=$url"
}

suspend fun getWebTitle(url: String): String {
    val baseUrl = extractBaseUrl(url)
    var title = fetchTitle(baseUrl)
    if (title == null) {
        title = fetchTitle(extractMainDomainUrl(baseUrl))
    }
    return title ?: "title not found"
}
fun extractBaseUrl(url: String): String {
    return try {
        val parsedUrl = URL(url)
        "${parsedUrl.protocol}://${parsedUrl.host}"
    } catch (e: Exception) {
        println("Invalid URL: $e")
        ""
    }
}

fun fetchTitle(url: String): String? {
    return try {
        val doc = Jsoup.connect(url).get()
        doc.title()
    } catch (e: Exception) {
        null
    }
}

fun extractUrl(text: String): String {
    val urlRegex = """https?:\/\/[^\s]+""".toRegex() // URL만 추출하는 정규식
    return urlRegex.find(text)?.value ?: ""
}

fun getMetaDescription(url: String): String {
    return try {
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .get()

        val metaTag = doc.select("meta[name=description]").first()

        metaTag?.attr("content") ?: "No meta description found."
    } catch (e: Exception) {
        "Failed to fetch page: ${e.message}"
    }
}

fun extractMainDomainUrl(url: String): String {
    val regex = Regex("""(?:https?://)?(?:www\.)?(([^./]+\.)?([^./]+\.[a-z]+))(?:/.*)?""")
    val mainDomain = regex.find(url)?.groupValues?.get(3)
    return mainDomain?.let { "https://$it/" } ?: url
}
fun convertTimestampToMonthDay(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("MM.dd")
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    return formatter.format(localDateTime)
}
fun getTodayStartTimestamp(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getYearMonth(timestamp: Long): Int {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }
    //val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작
    return month
}