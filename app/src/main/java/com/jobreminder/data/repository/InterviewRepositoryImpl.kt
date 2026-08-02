package com.jobreminder.data.repository

import com.jobreminder.data.local.dao.InterviewDao
import com.jobreminder.data.local.entity.InterviewEntity
import com.jobreminder.domain.model.Interview
import com.jobreminder.domain.repository.InterviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class InterviewRepositoryImpl(
    private val interviewDao: InterviewDao
) : InterviewRepository {

    override fun getAllInterviews(userId: String): Flow<List<Interview>> {
        return interviewDao.getAllInterviews(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getInterviewsByJobId(jobId: Long): Flow<List<Interview>> {
        return interviewDao.getInterviewsByJobId(jobId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUpcomingInterviews(userId: String): Flow<List<Interview>> {
        val today = LocalDate.now().toString()
        return interviewDao.getUpcomingInterviews(userId, today).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getInterviewById(interviewId: Long): Interview? {
        return interviewDao.getInterviewById(interviewId)?.toDomain()
    }

    override suspend fun insertInterview(interview: Interview): Long {
        return interviewDao.insertInterview(interview.toEntity())
    }

    override suspend fun updateInterview(interview: Interview) {
        interviewDao.updateInterview(interview.toEntity())
    }

    override suspend fun deleteInterview(interview: Interview) {
        interviewDao.deleteInterview(interview.toEntity())
    }

    override suspend fun deleteInterviewById(interviewId: Long) {
        interviewDao.deleteInterviewById(interviewId)
    }

    override suspend fun deleteInterviewsByJobId(jobId: Long) {
        interviewDao.deleteInterviewsByJobId(jobId)
    }

    private fun InterviewEntity.toDomain(): Interview {
        return Interview(
            id = id,
            userId = userId,
            jobId = jobId,
            interviewDate = interviewDate,
            type = type,
            location = location,
            interviewerName = interviewerName,
            notes = notes
        )
    }

    private fun Interview.toEntity(): InterviewEntity {
        return InterviewEntity(
            id = id,
            userId = userId,
            jobId = jobId,
            interviewDate = interviewDate,
            type = type,
            location = location,
            interviewerName = interviewerName,
            notes = notes
        )
    }
}