package com.jobreminder.ui.screens.jobdetail

import androidx.lifecycle.ViewModel
import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.model.JobStatus
import com.jobreminder.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class JobDetailViewModel(
    private val jobId: Long,
    private val jobRepository: JobRepository
) : ViewModel() {

    val job: Flow<JobApplication?>
        get() {
            return kotlinx.coroutines.flow.flow {
                emit(jobRepository.getJobById(jobId))
            }
        }

    suspend fun updateStatus(status: JobStatus) {
        jobRepository.getJobById(jobId)?.let { job ->
            jobRepository.updateJob(job.copy(status = status, updatedAt = java.time.LocalDateTime.now()))
        }
    }

    suspend fun deleteJob() {
        jobRepository.deleteJobById(jobId)
    }
}