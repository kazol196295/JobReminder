package com.jobreminder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jobreminder.ui.auth.LoginScreen
import com.jobreminder.ui.screens.addjob.AddJobScreen
import com.jobreminder.ui.screens.home.HomeScreen
import com.jobreminder.ui.screens.jobdetail.JobDetailScreen
import com.jobreminder.ui.screens.resume.ResumeScreen
import com.jobreminder.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object AddJob : Screen("add_job")
    object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: Long) = "job_detail/$jobId"
    }
    object Resume : Screen("resume")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onJobClick = { jobId ->
                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                },
                onAddJobClick = {
                    navController.navigate(Screen.AddJob.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.AddJob.route) {
            AddJobScreen(
                onJobSaved = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.JobDetail.route,
            arguments = listOf(navArgument("jobId") { type = NavType.LongType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getLong("jobId") ?: 0L
            JobDetailScreen(
                jobId = jobId,
                onBack = {
                    navController.popBackStack()
                },
                onEdit = {
                    navController.navigate(Screen.AddJob.route)
                }
            )
        }

        composable(Screen.Resume.route) {
            ResumeScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}