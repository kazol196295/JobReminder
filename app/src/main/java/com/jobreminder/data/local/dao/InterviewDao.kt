package com.jobreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jobreminder.data.local.entity.InterviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterviewDao {
    @Query("SELECT * FROM interviews WHERE userId = :userId ORDER BY interviewDate ASC")
    fun getAllInterviews(userId: String): Flow<List<InterviewEntity>>

    @Query("SELECT * FROM interviews WHERE jobId = :jobId ORDER BY interviewDate ASC")
    fun getInterviewsByJobId(jobId: Long): Flow<List<InterviewEntity>>

    @Query("SELECT * FROM interviews WHERE userId = :userId AND interviewDate >= :date ORDER BY interviewDate ASC")
    fun getUpcomingInterviews(userId: String, date: String): Flow<List<InterviewEntity>>

    @Query("SELECT * FROM interviews WHERE id = :interviewId")
    suspend fun getInterviewById(interviewId: Long): InterviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterview(interview: InterviewEntity): Long

    @Update
    suspend fun updateInterview(interview: InterviewEntity)

    @Delete
    suspend fun deleteInterview(interview: InterviewEntity)

    @Query("DELETE FROM interviews WHERE id = :interviewId")
    suspend fun deleteInterviewById(interviewId: Long)

    @Query("DELETE FROM interviews WHERE jobId = :jobId")
    suspend fun deleteInterviewsByJobId(jobId: Long)
}