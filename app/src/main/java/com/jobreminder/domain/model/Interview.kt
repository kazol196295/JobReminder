package com.jobreminder.domain.model

import java.time.LocalDateTime

data class Interview(
    val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val interviewDate: LocalDateTime,
    val type: InterviewType,
    val location: String? = null,
    val interviewerName: String? = null,
    val notes: String? = null
)