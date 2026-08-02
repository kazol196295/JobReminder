package com.jobreminder.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jobreminder.data.local.dao.ResumeDao
import com.jobreminder.data.local.entity.ResumeEntity
import com.jobreminder.domain.model.Education
import com.jobreminder.domain.model.Resume
import com.jobreminder.domain.model.WorkExperience
import com.jobreminder.domain.repository.ResumeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ResumeRepositoryImpl(
    private val resumeDao: ResumeDao
) : ResumeRepository {

    private val gson = Gson()

    override fun getAllResumes(userId: String): Flow<List<Resume>> {
        return resumeDao.getAllResumes(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getDefaultResume(userId: String): Resume? {
        return resumeDao.getDefaultResume(userId)?.toDomain()
    }

    override suspend fun getResumeById(resumeId: Long): Resume? {
        return resumeDao.getResumeById(resumeId)?.toDomain()
    }

    override suspend fun insertResume(resume: Resume): Long {
        return resumeDao.insertResume(resume.toEntity())
    }

    override suspend fun updateResume(resume: Resume) {
        resumeDao.updateResume(resume.toEntity())
    }

    override suspend fun deleteResume(resume: Resume) {
        resumeDao.deleteResume(resume.toEntity())
    }

    override suspend fun deleteResumeById(resumeId: Long) {
        resumeDao.deleteResumeById(resumeId)
    }

    override suspend fun setDefaultResume(resumeId: Long) {
        resumeDao.clearDefaultResumes(getResumeById(resumeId)?.userId ?: return)
        resumeDao.setDefaultResume(resumeId)
    }

    private fun ResumeEntity.toDomain(): Resume {
        val skillsList: List<String> = try {
            gson.fromJson(skills, object : TypeToken<List<String>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }

        val experienceList: List<WorkExperience> = try {
            gson.fromJson(experience, object : TypeToken<List<WorkExperience>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }

        val educationList: List<Education> = try {
            gson.fromJson(education, object : TypeToken<List<Education>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }

        return Resume(
            id = id,
            userId = userId,
            fullName = fullName,
            email = email,
            phone = phone,
            summary = summary,
            skills = skillsList,
            experience = experienceList,
            education = educationList,
            isDefault = isDefault,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun Resume.toEntity(): ResumeEntity {
        return ResumeEntity(
            id = id,
            userId = userId,
            fullName = fullName,
            email = email,
            phone = phone,
            summary = summary,
            skills = gson.toJson(skills),
            experience = gson.toJson(experience),
            education = gson.toJson(education),
            isDefault = isDefault,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}