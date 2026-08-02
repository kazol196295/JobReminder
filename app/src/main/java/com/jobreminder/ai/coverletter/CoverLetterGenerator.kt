package com.jobreminder.ai.coverletter

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jobreminder.domain.model.Resume
import com.jobreminder.util.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class CoverLetterGenerator(private val apiKey: String = ApiConfig.getMimoApiKey()) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun generate(
        jobDescription: String,
        companyName: String,
        position: String,
        resume: Resume
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = buildPrompt(jobDescription, companyName, position, resume)
                callMimoAPI(prompt)
            } catch (e: Exception) {
                generateLocally(jobDescription, companyName, position, resume)
            }
        }
    }

    private fun buildPrompt(
        jobDescription: String,
        companyName: String,
        position: String,
        resume: Resume
    ): String {
        val experienceText = resume.experience.joinToString("\n") {
            "- ${it.title} at ${it.company} (${it.duration}): ${it.description}"
        }
        val educationText = resume.education.joinToString("\n") {
            "- ${it.degree} from ${it.institution} (${it.year})"
        }
        val skillsText = resume.skills.joinToString(", ")

        return """
            Write a professional cover letter for:
            
            Position: $position
            Company: $companyName
            
            Job Description:
            $jobDescription
            
            My Background:
            Name: ${resume.fullName}
            Summary: ${resume.summary ?: "Not provided"}
            
            Skills: $skillsText
            
            Experience:
            $experienceText
            
            Education:
            $educationText
            
            Requirements:
            - Professional and engaging tone
            - Highlight relevant skills and experience that match the job
            - Show enthusiasm for the role and company
            - Keep it concise (3-4 paragraphs)
            - Include specific examples from my experience
            - End with a call to action
        """.trimIndent()
    }

    private fun callMimoAPI(prompt: String): String {
        val jsonBody = gson.toJson(mapOf(
            "model" to ApiConfig.MIMO_MODEL,
            "messages" to listOf(
                mapOf("role" to "system", "content" to "You are a professional cover letter writer. Create compelling, personalized cover letters."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.7,
            "max_tokens" to 1500
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

    private fun generateLocally(
        jobDescription: String,
        companyName: String,
        position: String,
        resume: Resume
    ): String {
        val topSkills = resume.skills.take(5).joinToString(", ")
        val recentExperience = resume.experience.firstOrNull()
        val experienceHighlight = if (recentExperience != null) {
            "In my recent role as ${recentExperience.title} at ${recentExperience.company}, ${recentExperience.description}"
        } else {
            "I have developed strong skills in $topSkills through my professional experience."
        }

        return """
            Dear Hiring Manager,
            
            I am writing to express my strong interest in the $position position at $companyName. With my background and skills, I believe I would be a valuable addition to your team.
            
            $experienceHighlight
            
            My key skills include $topSkills, which align well with the requirements outlined in your job posting. I am particularly excited about the opportunity to contribute to $companyName's mission and work on challenging projects.
            
            I am confident that my experience and passion make me an ideal candidate for this role. I would welcome the opportunity to discuss how my background and skills would benefit your team.
            
            Thank you for considering my application. I look forward to hearing from you.
            
            Best regards,
            ${resume.fullName}
            ${resume.email}
            ${resume.phone ?: ""}
        """.trimIndent()
    }
}