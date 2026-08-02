package com.jobreminder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jobreminder.domain.model.CompanyType
import com.jobreminder.domain.model.JobCategory
import com.jobreminder.domain.model.JobStatus
import com.jobreminder.domain.model.LocationType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "job_applications")
data class JobApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val companyName: String,
    val positionTitle: String,
    val jobCategory: JobCategory,
    val applicationDate: LocalDate,
    val status: JobStatus,
    val description: String? = null,
    val requirements: String? = null,
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