package com.jobreminder.domain.repository

import com.jobreminder.domain.model.Resume
import kotlinx.coroutines.flow.Flow

interface ResumeRepository {
    fun getAllResumes(userId: String): Flow<List<Resume>>
    suspend fun getDefaultResume(userId: String): Resume?
    suspend fun getResumeById(resumeId: Long): Resume?
    suspend fun insertResume(resume: Resume): Long
    suspend fun updateResume(resume: Resume)
    suspend fun deleteResume(resume: Resume)
    suspend fun deleteResumeById(resumeId: Long)
    suspend fun setDefaultResume(resumeId: Long)
}