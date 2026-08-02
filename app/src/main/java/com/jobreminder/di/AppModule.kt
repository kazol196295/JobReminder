package com.jobreminder.di

import com.jobreminder.ai.coverletter.CoverLetterGenerator
import com.jobreminder.ai.parser.JobExtractor
import com.jobreminder.ai.parser.UrlParser
import com.jobreminder.data.local.database.AppDatabase
import com.jobreminder.data.remote.firebase.auth.FirebaseAuthManager
import com.jobreminder.data.remote.firebase.firestore.FirestoreManager
import com.jobreminder.data.repository.InterviewRepositoryImpl
import com.jobreminder.data.repository.JobRepositoryImpl
import com.jobreminder.data.repository.ReminderRepositoryImpl
import com.jobreminder.data.repository.ResumeRepositoryImpl
import com.jobreminder.domain.repository.InterviewRepository
import com.jobreminder.domain.repository.JobRepository
import com.jobreminder.domain.repository.ReminderRepository
import com.jobreminder.domain.repository.ResumeRepository
import com.jobreminder.ui.screens.addjob.AddJobViewModel
import com.jobreminder.ui.screens.home.HomeViewModel
import com.jobreminder.ui.screens.jobdetail.JobDetailViewModel
import com.jobreminder.ui.screens.resume.ResumeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().jobApplicationDao() }
    single { get<AppDatabase>().reminderDao() }
    single { get<AppDatabase>().interviewDao() }
    single { get<AppDatabase>().resumeDao() }
}

val repositoryModule = module {
    single<JobRepository> { JobRepositoryImpl(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }
    single<InterviewRepository> { InterviewRepositoryImpl(get()) }
    single<ResumeRepository> { ResumeRepositoryImpl(get()) }
}

val firebaseModule = module {
    single { FirebaseAuthManager() }
}

val firestoreModule = module {
    single { FirestoreManager() }
}

val aiModule = module {
    single { UrlParser() }
    single { JobExtractor() }
    single { CoverLetterGenerator() }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { AddJobViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { (jobId: Long) -> JobDetailViewModel(jobId, get()) }
    viewModel { ResumeViewModel(get(), get()) }
}

val appModule = module {
    includes(databaseModule, repositoryModule, aiModule, firebaseModule, firestoreModule, viewModelModule)
}