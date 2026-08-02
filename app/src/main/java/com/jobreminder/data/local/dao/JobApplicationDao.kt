package com.jobreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jobreminder.data.local.entity.JobApplicationEntity
import com.jobreminder.domain.model.JobStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface JobApplicationDao {
    @Query("SELECT * FROM job_applications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllJobs(userId: String): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE userId = :userId AND status = :status ORDER BY createdAt DESC")
    fun getJobsByStatus(userId: String, status: JobStatus): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE userId = :userId AND deadline <= :date AND status != 'APPLIED' ORDER BY deadline ASC")
    fun getJobsWithUpcomingDeadlines(userId: String, date: String): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE id = :jobId")
    suspend fun getJobById(jobId: Long): JobApplicationEntity?

    @Query("SELECT * FROM job_applications WHERE userId = :userId AND (companyName LIKE '%' || :query || '%' OR positionTitle LIKE '%' || :query || '%')")
    fun searchJobs(userId: String, query: String): Flow<List<JobApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobApplicationEntity): Long

    @Update
    suspend fun updateJob(job: JobApplicationEntity)

    @Delete
    suspend fun deleteJob(job: JobApplicationEntity)

    @Query("DELETE FROM job_applications WHERE id = :jobId")
    suspend fun deleteJobById(jobId: Long)

    @Query("SELECT COUNT(*) FROM job_applications WHERE userId = :userId")
    suspend fun getJobCount(userId: String): Int

    @Query("SELECT COUNT(*) FROM job_applications WHERE userId = :userId AND status = :status")
    suspend fun getJobCountByStatus(userId: String, status: JobStatus): Int
}