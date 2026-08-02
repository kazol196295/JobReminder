package com.jobreminder.domain.model

import java.time.LocalDateTime

data class Resume(
    val id: Long = 0,
    val userId: String,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val summary: String? = null,
    val skills: List<String> = emptyList(),
    val experience: List<WorkExperience> = emptyList(),
    val education: List<Education> = emptyList(),
    val isDefault: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class WorkExperience(
    val title: String,
    val company: String,
    val duration: String,
    val description: String
)

data class Education(
    val degree: String,
    val institution: String,
    val year: String
)