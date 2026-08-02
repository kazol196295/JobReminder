package com.jobreminder.domain.model

import java.time.LocalDateTime

data class Reminder(
    val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val title: String,
    val reminderDate: LocalDateTime,
    val isCompleted: Boolean = false,
    val type: ReminderType
)