package com.jobreminder.ai.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

class UrlParser {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun fetchContent(url: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build()
                val response = client.newCall(request).execute()
                val html = response.body?.string() ?: ""
                parseHtml(html)
            } catch (e: Exception) {
                throw Exception("Failed to fetch URL: ${e.message}")
            }
        }
    }

    private fun parseHtml(html: String): String {
        val doc = Jsoup.parse(html)
        doc.select("script, style, nav, footer, header, aside, .sidebar, .menu, .navigation").remove()
        val main = doc.select("main, article, .job-description, .content, .job-details, .posting-details, [class*='job'], [class*='position']").first()
        val text = main?.text() ?: doc.body()?.text() ?: ""
        return cleanText(text)
    }

    private fun cleanText(text: String): String {
        return text
            .replace(Regex("\\s+"), " ")
            .replace(Regex("[\\r\\n]+"), "\n")
            .trim()
            .take(10000)
    }
}