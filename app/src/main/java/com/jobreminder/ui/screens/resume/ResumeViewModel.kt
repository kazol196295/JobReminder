package com.jobreminder.ui.screens.resume

import androidx.lifecycle.ViewModel
import com.jobreminder.data.remote.firebase.auth.FirebaseAuthManager
import com.jobreminder.domain.model.Education
import com.jobreminder.domain.model.Resume
import com.jobreminder.domain.model.WorkExperience
import com.jobreminder.domain.repository.ResumeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDateTime

class ResumeViewModel(
    private val resumeRepository: ResumeRepository,
    private val authManager: FirebaseAuthManager
) : ViewModel() {

    val resumes: Flow<List<Resume>>
        get() {
            val userId = authManager.getCurrentUserId() ?: return emptyFlow()
            return resumeRepository.getAllResumes(userId)
        }

    suspend fun saveResume(
        fullName: String,
        email: String,
        phone: String?,
        summary: String?,
        skills: List<String>,
        experience: List<WorkExperience>,
        education: List<Education>
    ): Result<Long> {
        return try {
            val userId = authManager.getCurrentUserId() ?: throw Exception("User not logged in")
            val resume = Resume(
                userId = userId,
                fullName = fullName,
                email = email,
                phone = phone,
                summary = summary,
                skills = skills,
                experience = experience,
                education = education,
                isDefault = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            val resumeId = resumeRepository.insertResume(resume)
            Result.success(resumeId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteResume(resume: Resume): Result<Unit> {
        return try {
            resumeRepository.deleteResume(resume)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setDefaultResume(resumeId: Long): Result<Unit> {
        return try {
            resumeRepository.setDefaultResume(resumeId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}