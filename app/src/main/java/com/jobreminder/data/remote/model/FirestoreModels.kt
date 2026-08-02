package com.jobreminder.data.remote.model

import com.google.firebase.Timestamp

data class FirestoreJobApplication(
    val id: String = "",
    val companyName: String = "",
    val positionTitle: String = "",
    val jobCategory: String = "",
    val applicationDate: Timestamp? = null,
    val status: String = "",
    val description: String? = null,
    val requirements: String? = null,
    val salaryRange: String? = null,
    val locationType: String = "",
    val companyType: String = "",
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val deadline: Timestamp? = null,
    val coverLetter: String? = null,
    val notes: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

data class FirestoreResume(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String? = null,
    val summary: String? = null,
    val skills: List<String> = emptyList(),
    val experience: List<FirestoreWorkExperience> = emptyList(),
    val education: List<FirestoreEducation> = emptyList(),
    val isDefault: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

data class FirestoreWorkExperience(
    val title: String = "",
    val company: String = "",
    val duration: String = "",
    val description: String = ""
)

data class FirestoreEducation(
    val degree: String = "",
    val institution: String = "",
    val year: String = ""
)