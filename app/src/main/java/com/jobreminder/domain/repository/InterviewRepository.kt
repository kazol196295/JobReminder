package com.jobreminder.domain.repository

import com.jobreminder.domain.model.Interview
import kotlinx.coroutines.flow.Flow

interface InterviewRepository {
    fun getAllInterviews(userId: String): Flow<List<Interview>>
    fun getInterviewsByJobId(jobId: Long): Flow<List<Interview>>
    fun getUpcomingInterviews(userId: String): Flow<List<Interview>>
    suspend fun getInterviewById(interviewId: Long): Interview?
    suspend fun insertInterview(interview: Interview): Long
    suspend fun updateInterview(interview: Interview)
    suspend fun deleteInterview(interview: Interview)
    suspend fun deleteInterviewById(interviewId: Long)
    suspend fun deleteInterviewsByJobId(jobId: Long)
}