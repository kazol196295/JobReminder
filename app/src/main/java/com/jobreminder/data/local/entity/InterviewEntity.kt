package com.jobreminder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jobreminder.domain.model.InterviewType
import java.time.LocalDateTime

@Entity(tableName = "interviews")
data class InterviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val interviewDate: LocalDateTime,
    val type: InterviewType,
    val location: String? = null,
    val interviewerName: String? = null,
    val notes: String? = null
)