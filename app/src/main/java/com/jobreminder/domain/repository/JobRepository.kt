package com.jobreminder.domain.repository

import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.model.JobStatus
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    fun getAllJobs(userId: String): Flow<List<JobApplication>>
    fun getJobsByStatus(userId: String, status: JobStatus): Flow<List<JobApplication>>
    fun getJobsWithUpcomingDeadlines(userId: String): Flow<List<JobApplication>>
    fun searchJobs(userId: String, query: String): Flow<List<JobApplication>>
    suspend fun getJobById(jobId: Long): JobApplication?
    suspend fun insertJob(job: JobApplication): Long
    suspend fun updateJob(job: JobApplication)
    suspend fun deleteJob(job: JobApplication)
    suspend fun deleteJobById(jobId: Long)
    suspend fun getJobCount(userId: String): Int
    suspend fun getJobCountByStatus(userId: String, status: JobStatus): Int
}