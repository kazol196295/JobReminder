package com.jobreminder.data.repository

import com.jobreminder.data.local.dao.JobApplicationDao
import com.jobreminder.data.local.entity.JobApplicationEntity
import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.model.JobStatus
import com.jobreminder.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class JobRepositoryImpl(
    private val jobApplicationDao: JobApplicationDao
) : JobRepository {

    override fun getAllJobs(userId: String): Flow<List<JobApplication>> {
        return jobApplicationDao.getAllJobs(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getJobsByStatus(userId: String, status: JobStatus): Flow<List<JobApplication>> {
        return jobApplicationDao.getJobsByStatus(userId, status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getJobsWithUpcomingDeadlines(userId: String): Flow<List<JobApplication>> {
        val today = LocalDate.now().toString()
        return jobApplicationDao.getJobsWithUpcomingDeadlines(userId, today).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchJobs(userId: String, query: String): Flow<List<JobApplication>> {
        return jobApplicationDao.searchJobs(userId, query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getJobById(jobId: Long): JobApplication? {
        return jobApplicationDao.getJobById(jobId)?.toDomain()
    }

    override suspend fun insertJob(job: JobApplication): Long {
        return jobApplicationDao.insertJob(job.toEntity())
    }

    override suspend fun updateJob(job: JobApplication) {
        jobApplicationDao.updateJob(job.toEntity())
    }

    override suspend fun deleteJob(job: JobApplication) {
        jobApplicationDao.deleteJob(job.toEntity())
    }

    override suspend fun deleteJobById(jobId: Long) {
        jobApplicationDao.deleteJobById(jobId)
    }

    override suspend fun getJobCount(userId: String): Int {
        return jobApplicationDao.getJobCount(userId)
    }

    override suspend fun getJobCountByStatus(userId: String, status: JobStatus): Int {
        return jobApplicationDao.getJobCountByStatus(userId, status)
    }

    private fun JobApplicationEntity.toDomain(): JobApplication {
        return JobApplication(
            id = id,
            userId = userId,
            companyName = companyName,
            positionTitle = positionTitle,
            jobCategory = jobCategory,
            applicationDate = applicationDate,
            status = status,
            description = description,
            requirements = requirements?.split(",")?.map { it.trim() } ?: emptyList(),
            salaryRange = salaryRange,
            locationType = locationType,
            companyType = companyType,
            applyLink = applyLink,
            applyEmail = applyEmail,
            deadline = deadline,
            coverLetter = coverLetter,
            notes = notes,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun JobApplication.toEntity(): JobApplicationEntity {
        return JobApplicationEntity(
            id = id,
            userId = userId,
            companyName = companyName,
            positionTitle = positionTitle,
            jobCategory = jobCategory,
            applicationDate = applicationDate,
            status = status,
            description = description,
            requirements = requirements.joinToString(", "),
            salaryRange = salaryRange,
            locationType = locationType,
            companyType = companyType,
            applyLink = applyLink,
            applyEmail = applyEmail,
            deadline = deadline,
            coverLetter = coverLetter,
            notes = notes,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}