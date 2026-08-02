package com.jobreminder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jobreminder.domain.model.ReminderType
import java.time.LocalDateTime

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val title: String,
    val reminderDate: LocalDateTime,
    val isCompleted: Boolean = false,
    val type: ReminderType
)