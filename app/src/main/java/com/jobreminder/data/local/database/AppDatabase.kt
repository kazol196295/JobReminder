package com.jobreminder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jobreminder.data.local.dao.InterviewDao
import com.jobreminder.data.local.dao.JobApplicationDao
import com.jobreminder.data.local.dao.ReminderDao
import com.jobreminder.data.local.dao.ResumeDao
import com.jobreminder.data.local.entity.InterviewEntity
import com.jobreminder.data.local.entity.JobApplicationEntity
import com.jobreminder.data.local.entity.ReminderEntity
import com.jobreminder.data.local.entity.ResumeEntity

@Database(
    entities = [
        JobApplicationEntity::class,
        ReminderEntity::class,
        InterviewEntity::class,
        ResumeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobApplicationDao(): JobApplicationDao
    abstract fun reminderDao(): ReminderDao
    abstract fun interviewDao(): InterviewDao
    abstract fun resumeDao(): ResumeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "job_reminder_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}