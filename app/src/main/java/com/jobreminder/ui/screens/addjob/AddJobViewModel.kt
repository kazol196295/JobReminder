package com.jobreminder.ui.screens.addjob

import androidx.lifecycle.ViewModel
import com.jobreminder.ai.coverletter.CoverLetterGenerator
import com.jobreminder.ai.model.ParsedJobData
import com.jobreminder.ai.parser.JobExtractor
import com.jobreminder.ai.parser.UrlParser
import com.jobreminder.data.remote.firebase.auth.FirebaseAuthManager
import com.jobreminder.domain.model.CompanyType
import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.model.JobCategory
import com.jobreminder.domain.model.JobStatus
import com.jobreminder.domain.model.LocationType
import com.jobreminder.domain.repository.JobRepository
import com.jobreminder.domain.repository.ResumeRepository
import java.time.LocalDate
import java.time.LocalDateTime

class AddJobViewModel(
    private val jobRepository: JobRepository,
    private val resumeRepository: ResumeRepository,
    private val urlParser: UrlParser,
    private val jobExtractor: JobExtractor,
    private val coverLetterGenerator: CoverLetterGenerator,
    private val authManager: FirebaseAuthManager
) : ViewModel() {

    suspend fun parseJob(input: String, isLink: Boolean): Result<ParsedJobData> {
        return try {
            val content = if (isLink) {
                urlParser.fetchContent(input)
            } else {
                input
            }
            val parsedData = jobExtractor.extractJobData(content)
            Result.success(parsedData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveJob(
        companyName: String,
        positionTitle: String,
        jobCategory: JobCategory,
        salaryRange: String?,
        deadline: String?,
        applyLink: String?,
        applyEmail: String?,
        locationType: LocationType,
        companyType: CompanyType,
        description: String?
    ): Result<Long> {
        return try {
            val userId = authManager.getCurrentUserId() ?: throw Exception("User not logged in")
            val job = JobApplication(
                userId = userId,
                companyName = companyName,
                positionTitle = positionTitle,
                jobCategory = jobCategory,
                applicationDate = LocalDate.now(),
                status = JobStatus.DRAFT,
                description = description,
                salaryRange = salaryRange,
                locationType = locationType,
                companyType = companyType,
                applyLink = applyLink,
                applyEmail = applyEmail,
                deadline = deadline?.let { LocalDate.parse(it) },
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            val jobId = jobRepository.insertJob(job)
            Result.success(jobId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateCoverLetter(
        jobDescription: String,
        companyName: String,
        position: String
    ): Result<String> {
        return try {
            val userId = authManager.getCurrentUserId() ?: throw Exception("User not logged in")
            val resume = resumeRepository.getDefaultResume(userId)
                ?: throw Exception("No resume found. Please add a resume first.")
            val coverLetter = coverLetterGenerator.generate(
                jobDescription = jobDescription,
                companyName = companyName,
                position = position,
                resume = resume
            )
            Result.success(coverLetter)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}