package com.jobreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jobreminder.data.local.entity.ResumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {
    @Query("SELECT * FROM resumes WHERE userId = :userId ORDER BY isDefault DESC, updatedAt DESC")
    fun getAllResumes(userId: String): Flow<List<ResumeEntity>>

    @Query("SELECT * FROM resumes WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultResume(userId: String): ResumeEntity?

    @Query("SELECT * FROM resumes WHERE id = :resumeId")
    suspend fun getResumeById(resumeId: Long): ResumeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: ResumeEntity): Long

    @Update
    suspend fun updateResume(resume: ResumeEntity)

    @Delete
    suspend fun deleteResume(resume: ResumeEntity)

    @Query("DELETE FROM resumes WHERE id = :resumeId")
    suspend fun deleteResumeById(resumeId: Long)

    @Query("UPDATE resumes SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultResumes(userId: String)

    @Query("UPDATE resumes SET isDefault = 1 WHERE id = :resumeId")
    suspend fun setDefaultResume(resumeId: Long)
}