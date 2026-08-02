package com.jobreminder.ai.parser

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jobreminder.ai.model.ParsedJobData
import com.jobreminder.domain.model.CompanyType
import com.jobreminder.domain.model.JobCategory
import com.jobreminder.domain.model.LocationType
import com.jobreminder.util.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class JobExtractor(private val apiKey: String = ApiConfig.getMimoApiKey()) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun extractJobData(content: String): ParsedJobData {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = buildPrompt(content)
                val response = callMimoAPI(prompt)
                parseResponse(response)
            } catch (e: Exception) {
                parseLocally(content)
            }
        }
    }

    private fun buildPrompt(content: String): String {
        return """
            Extract job posting details from this text. Return ONLY valid JSON with these fields:
            - company_name: string
            - position_title: string
            - job_category: one of [AI_ENGINEER, ML_ENGINEER, DATA_SCIENCE, FULL_STACK, BACKEND, FRONTEND, DEVOPS, MOBILE, CLOUD, SECURITY, OTHER]
            - salary_range: string or null
            - deadline: ISO date string (YYYY-MM-DD) or null
            - apply_link: URL string or null
            - apply_email: email string or null
            - location_type: one of [REMOTE, ONSITE, HYBRID]
            - company_type: one of [MULTINATIONAL, STARTUP, NATIVE]
            - description: string (brief summary)
            - requirements: array of strings
            
            Text:
            $content
        """.trimIndent()
    }

    private fun callMimoAPI(prompt: String): String {
        val jsonBody = gson.toJson(mapOf(
            "model" to ApiConfig.MIMO_MODEL,
            "messages" to listOf(
                mapOf("role" to "system", "content" to "You are a job posting parser. Extract structured data from job descriptions. Return ONLY valid JSON, no other text."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.1,
            "max_tokens" to 1000
        ))

        val request = Request.Builder()
            .url(ApiConfig.MIMO_API_URL)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response from Mimo API")
        
        val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
        
        return when {
            jsonResponse.has("choices") -> {
                jsonResponse
                    .getAsJsonArray("choices")
                    .get(0)
                    .asJsonObject
                    .getAsJsonObject("message")
                    .get("content")
                    .asString
            }
            jsonResponse.has("response") -> {
                jsonResponse.get("response").asString
            }
            jsonResponse.has("content") -> {
                jsonResponse.get("content").asString
            }
            else -> throw Exception("Unexpected Mimo API response format: $responseBody")
        }
    }

    private fun parseResponse(response: String): ParsedJobData {
        return try {
            val jsonStr = response.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val json = gson.fromJson(jsonStr, JsonObject::class.java)
            ParsedJobData(
                companyName = json.get("company_name")?.asString ?: "",
                positionTitle = json.get("position_title")?.asString ?: "",
                jobCategory = JobCategory.fromString(json.get("job_category")?.asString ?: "OTHER"),
                salaryRange = json.get("salary_range")?.asString,
                deadline = json.get("deadline")?.asString,
                applyLink = json.get("apply_link")?.asString,
                applyEmail = json.get("apply_email")?.asString,
                locationType = LocationType.fromString(json.get("location_type")?.asString ?: "UNKNOWN"),
                companyType = CompanyType.fromString(json.get("company_type")?.asString ?: "UNKNOWN"),
                description = json.get("description")?.asString ?: "",
                requirements = json.getAsJsonArray("requirements")?.map { it.asString } ?: emptyList(),
                confidence = 0.9f
            )
        } catch (e: Exception) {
            ParsedJobData(confidence = 0f)
        }
    }

    private fun parseLocally(content: String): ParsedJobData {
        val lowerContent = content.lowercase()
        
        val locationType = when {
            lowerContent.contains("remote") -> LocationType.REMOTE
            lowerContent.contains("onsite") || lowerContent.contains("on-site") -> LocationType.ONSITE
            lowerContent.contains("hybrid") -> LocationType.HYBRID
            else -> LocationType.UNKNOWN
        }

        val companyType = when {
            lowerContent.contains("multinational") || lowerContent.contains("global") -> CompanyType.MULTINATIONAL
            lowerContent.contains("startup") -> CompanyType.STARTUP
            else -> CompanyType.NATIVE
        }

        val jobCategory = when {
            lowerContent.contains("ai engineer") || lowerContent.contains("artificial intelligence") -> JobCategory.AI_ENGINEER
            lowerContent.contains("ml engineer") || lowerContent.contains("machine learning") -> JobCategory.ML_ENGINEER
            lowerContent.contains("data science") || lowerContent.contains("data scientist") -> JobCategory.DATA_SCIENCE
            lowerContent.contains("full stack") || lowerContent.contains("fullstack") -> JobCategory.FULL_STACK
            lowerContent.contains("backend") || lowerContent.contains("back-end") -> JobCategory.BACKEND
            lowerContent.contains("frontend") || lowerContent.contains("front-end") -> JobCategory.FRONTEND
            lowerContent.contains("devops") -> JobCategory.DEVOPS
            lowerContent.contains("mobile") || lowerContent.contains("android") || lowerContent.contains("ios") -> JobCategory.MOBILE
            else -> JobCategory.OTHER
        }

        val salaryRegex = Regex("\\$[\\d,]+(?:\\s*-\\s*\\$[\\d,]+)?(?:\\s*/\\s*(?:year|month|hour))?")
        val salaryRange = salaryRegex.find(content)?.value

        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val applyEmail = emailRegex.find(content)?.value

        val urlRegex = Regex("https?://[^\\s]+")
        val applyLink = urlRegex.find(content)?.value

        return ParsedJobData(
            companyName = "",
            positionTitle = "",
            jobCategory = jobCategory,
            salaryRange = salaryRange,
            deadline = null,
            applyLink = applyLink,
            applyEmail = applyEmail,
            locationType = locationType,
            companyType = companyType,
            description = content.take(500),
            requirements = emptyList(),
            confidence = 0.5f
        )
    }
}