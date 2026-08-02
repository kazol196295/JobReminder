package com.jobreminder

import android.app.Application
import com.jobreminder.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JobReminderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@JobReminderApp)
            modules(appModule)
        }
    }
}