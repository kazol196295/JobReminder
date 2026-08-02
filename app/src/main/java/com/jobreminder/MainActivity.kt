package com.jobreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jobreminder.data.remote.firebase.auth.FirebaseAuthManager
import com.jobreminder.ui.navigation.AppNavigation
import com.jobreminder.ui.navigation.Screen
import com.jobreminder.ui.theme.JobReminderTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authManager: FirebaseAuthManager = koinInject()
                    
                    val startDestination = if (authManager.isLoggedIn()) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }

                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}