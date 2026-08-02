package com.jobreminder.ui.screens.resume

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobreminder.domain.model.Education
import com.jobreminder.domain.model.Resume
import com.jobreminder.domain.model.WorkExperience
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    onBack: () -> Unit,
    viewModel: ResumeViewModel = koinViewModel()
) {
    val resumes by viewModel.resumes.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showAddForm by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var experienceTitle by remember { mutableStateOf("") }
    var experienceCompany by remember { mutableStateOf("") }
    var experienceDuration by remember { mutableStateOf("") }
    var experienceDescription by remember { mutableStateOf("") }
    var educationDegree by remember { mutableStateOf("") }
    var educationInstitution by remember { mutableStateOf("") }
    var educationYear by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Resume") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddForm = !showAddForm }) {
                Icon(Icons.Default.Add, contentDescription = "Add Resume")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (resumes.isEmpty() && !showAddForm) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No Resume Added",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your resume to enable AI-powered cover letter generation.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            resumes.forEach { resume ->
                ResumeCard(
                    resume = resume,
                    onDelete = {
                        scope.launch {
                            viewModel.deleteResume(resume)
                        }
                    },
                    onSetDefault = {
                        scope.launch {
                            viewModel.setDefaultResume(resume.id)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (showAddForm) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Add New Resume",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Professional Summary") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Skills (comma-separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Work Experience",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = experienceTitle,
                    onValueChange = { experienceTitle = it },
                    label = { Text("Job Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = experienceCompany,
                    onValueChange = { experienceCompany = it },
                    label = { Text("Company") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = experienceDuration,
                    onValueChange = { experienceDuration = it },
                    label = { Text("Duration (e.g., 2020-2023)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = experienceDescription,
                    onValueChange = { experienceDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Education",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = educationDegree,
                    onValueChange = { educationDegree = it },
                    label = { Text("Degree") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = educationInstitution,
                    onValueChange = { educationInstitution = it },
                    label = { Text("Institution") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = educationYear,
                    onValueChange = { educationYear = it },
                    label = { Text("Year") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showAddForm = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.saveResume(
                                    fullName = fullName,
                                    email = email,
                                    phone = phone,
                                    summary = summary,
                                    skills = skills.split(",").map { it.trim() },
                                    experience = listOf(
                                        WorkExperience(
                                            title = experienceTitle,
                                            company = experienceCompany,
                                            duration = experienceDuration,
                                            description = experienceDescription
                                        )
                                    ),
                                    education = listOf(
                                        Education(
                                            degree = educationDegree,
                                            institution = educationInstitution,
                                            year = educationYear
                                        )
                                    )
                                )
                                showAddForm = false
                                fullName = ""
                                email = ""
                                phone = ""
                                summary = ""
                                skills = ""
                                experienceTitle = ""
                                experienceCompany = ""
                                experienceDuration = ""
                                experienceDescription = ""
                                educationDegree = ""
                                educationInstitution = ""
                                educationYear = ""
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Resume")
                    }
                }
            }
        }
    }
}

@Composable
fun ResumeCard(
    resume: Resume,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = resume.fullName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = resume.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    if (!resume.isDefault) {
                        IconButton(onClick = onSetDefault) {
                            Text("Set Default", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }

            if (resume.isDefault) {
                Text(
                    text = "Default Resume",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (resume.summary != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = resume.summary!!,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
        }
    }
}