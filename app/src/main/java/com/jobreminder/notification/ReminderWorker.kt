package com.jobreminder.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jobreminder.data.local.database.AppDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val notificationHelper = NotificationHelper(applicationContext)

        val jobs = database.jobApplicationDao().getAllJobs("")
        
        jobs.collect { jobList ->
            val today = LocalDate.now()
            
            jobList.forEach { job ->
                if (job.deadline != null) {
                    val daysUntilDeadline = ChronoUnit.DAYS.between(today, job.deadline)
                    
                    when {
                        daysUntilDeadline == 7L -> {
                            notificationHelper.showDeadlineNotification(
                                notificationId = job.id.toInt(),
                                title = "Deadline Approaching",
                                message = "${job.positionTitle} at ${job.companyName} - 7 days left to apply"
                            )
                        }
                        daysUntilDeadline == 3L -> {
                            notificationHelper.showDeadlineNotification(
                                notificationId = job.id.toInt(),
                                title = "Deadline Soon",
                                message = "${job.positionTitle} at ${job.companyName} - 3 days left to apply"
                            )
                        }
                        daysUntilDeadline == 1L -> {
                            notificationHelper.showDeadlineNotification(
                                notificationId = job.id.toInt(),
                                title = "Deadline Tomorrow",
                                message = "${job.positionTitle} at ${job.companyName} - Apply today!"
                            )
                        }
                        daysUntilDeadline == 0L -> {
                            notificationHelper.showDeadlineNotification(
                                notificationId = job.id.toInt(),
                                title = "Deadline Today!",
                                message = "${job.positionTitle} at ${job.companyName} - Apply now!"
                            )
                        }
                    }
                }
            }
        }

        return Result.success()
    }
}