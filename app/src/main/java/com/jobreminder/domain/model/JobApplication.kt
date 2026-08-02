package com.jobreminder.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class JobApplication(
    val id: Long = 0,
    val userId: String,
    val companyName: String,
    val positionTitle: String,
    val jobCategory: JobCategory,
    val applicationDate: LocalDate,
    val status: JobStatus,
    val description: String? = null,
    val requirements: List<String> = emptyList(),
    val salaryRange: String? = null,
    val locationType: LocationType,
    val companyType: CompanyType,
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val deadline: LocalDate? = null,
    val coverLetter: String? = null,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)