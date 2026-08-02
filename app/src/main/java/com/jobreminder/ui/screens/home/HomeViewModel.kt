package com.jobreminder.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobreminder.data.remote.firebase.auth.FirebaseAuthManager
import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val jobRepository: JobRepository,
    private val authManager: FirebaseAuthManager
) : ViewModel() {
    val jobs: Flow<List<JobApplication>>
        get() {
            val userId = authManager.getCurrentUserId() ?: return emptyFlow()
            return jobRepository.getAllJobs(userId)
        }

    val upcomingDeadlines: Flow<List<JobApplication>>
        get() {
            val userId = authManager.getCurrentUserId() ?: return emptyFlow()
            return jobRepository.getJobsWithUpcomingDeadlines(userId)
        }
}