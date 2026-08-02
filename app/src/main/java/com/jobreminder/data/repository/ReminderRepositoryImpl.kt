package com.jobreminder.data.repository

import com.jobreminder.data.local.dao.ReminderDao
import com.jobreminder.data.local.entity.ReminderEntity
import com.jobreminder.domain.model.Reminder
import com.jobreminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getAllReminders(userId: String): Flow<List<Reminder>> {
        return reminderDao.getAllReminders(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRemindersByJobId(jobId: Long): Flow<List<Reminder>> {
        return reminderDao.getRemindersByJobId(jobId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPendingReminders(userId: String): Flow<List<Reminder>> {
        return reminderDao.getPendingReminders(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getReminderById(reminderId: Long): Reminder? {
        return reminderDao.getReminderById(reminderId)?.toDomain()
    }

    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder.toEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toEntity())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder.toEntity())
    }

    override suspend fun deleteReminderById(reminderId: Long) {
        reminderDao.deleteReminderById(reminderId)
    }

    override suspend fun deleteRemindersByJobId(jobId: Long) {
        reminderDao.deleteRemindersByJobId(jobId)
    }

    private fun ReminderEntity.toDomain(): Reminder {
        return Reminder(
            id = id,
            userId = userId,
            jobId = jobId,
            title = title,
            reminderDate = reminderDate,
            isCompleted = isCompleted,
            type = type
        )
    }

    private fun Reminder.toEntity(): ReminderEntity {
        return ReminderEntity(
            id = id,
            userId = userId,
            jobId = jobId,
            title = title,
            reminderDate = reminderDate,
            isCompleted = isCompleted,
            type = type
        )
    }
}