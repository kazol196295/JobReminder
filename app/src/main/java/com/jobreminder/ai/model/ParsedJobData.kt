package com.jobreminder.ai.model

import com.jobreminder.domain.model.CompanyType
import com.jobreminder.domain.model.JobCategory
import com.jobreminder.domain.model.LocationType

data class ParsedJobData(
    val companyName: String = "",
    val positionTitle: String = "",
    val jobCategory: JobCategory = JobCategory.OTHER,
    val salaryRange: String? = null,
    val deadline: String? = null,
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val locationType: LocationType = LocationType.UNKNOWN,
    val companyType: CompanyType = CompanyType.UNKNOWN,
    val description: String = "",
    val requirements: List<String> = emptyList(),
    val confidence: Float = 0f
)