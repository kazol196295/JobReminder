package com.jobreminder.domain.repository

import com.jobreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(userId: String): Flow<List<Reminder>>
    fun getRemindersByJobId(jobId: Long): Flow<List<Reminder>>
    fun getPendingReminders(userId: String): Flow<List<Reminder>>
    suspend fun getReminderById(reminderId: Long): Reminder?
    suspend fun insertReminder(reminder: Reminder): Long
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteReminderById(reminderId: Long)
    suspend fun deleteRemindersByJobId(jobId: Long)
}