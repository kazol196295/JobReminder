package com.jobreminder.ui.screens.addjob

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobreminder.ai.model.ParsedJobData
import com.jobreminder.domain.model.CompanyType
import com.jobreminder.domain.model.JobApplication
import com.jobreminder.domain.model.JobCategory
import com.jobreminder.domain.model.JobStatus
import com.jobreminder.domain.model.LocationType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobScreen(
    onJobSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddJobViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    var inputMethod by remember { mutableStateOf(InputMethod.LINK) }
    var inputText by remember { mutableStateOf("") }
    var isParsing by remember { mutableStateOf(false) }
    var parsedData by remember { mutableStateOf<ParsedJobData?>(null) }

    var companyName by remember { mutableStateOf("") }
    var positionTitle by remember { mutableStateOf("") }
    var jobCategory by remember { mutableStateOf(JobCategory.OTHER) }
    var salaryRange by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var applyLink by remember { mutableStateOf("") }
    var applyEmail by remember { mutableStateOf("") }
    var locationType by remember { mutableStateOf(LocationType.UNKNOWN) }
    var companyType by remember { mutableStateOf(CompanyType.UNKNOWN) }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Job") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "How would you like to add the job?",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = inputMethod == InputMethod.LINK,
                    onClick = { inputMethod = InputMethod.LINK },
                    label = { Text("Link") }
                )
                FilterChip(
                    selected = inputMethod == InputMethod.TEXT,
                    onClick = { inputMethod = InputMethod.TEXT },
                    label = { Text("Text") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = {
                    Text(
                        if (inputMethod == InputMethod.LINK) "Paste job posting URL"
                        else "Paste job description text"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (inputMethod == InputMethod.TEXT) 5 else 1,
                maxLines = if (inputMethod == InputMethod.TEXT) 10 else 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isParsing = true
                    scope.launch {
                        val result = viewModel.parseJob(inputText, inputMethod == InputMethod.LINK)
                        result.fold(
                            onSuccess = { data ->
                                parsedData = data
                                companyName = data.companyName
                                positionTitle = data.positionTitle
                                jobCategory = data.jobCategory
                                salaryRange = data.salaryRange ?: ""
                                deadline = data.deadline ?: ""
                                applyLink = data.applyLink ?: ""
                                applyEmail = data.applyEmail ?: ""
                                locationType = data.locationType
                                companyType = data.companyType
                                description = data.description
                            },
                            onFailure = { /* Handle error */ }
                        )
                        isParsing = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = inputText.isNotBlank() && !isParsing
            ) {
                if (isParsing) {
                    CircularProgressIndicator(modifier = Modifier.height(24.dp))
                } else {
                    Text("Parse Job")
                }
            }

            if (parsedData != null) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Extracted Information",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = positionTitle,
                    onValueChange = { positionTitle = it },
                    label = { Text("Position Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                var categoryExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = jobCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Job Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        JobCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    jobCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = salaryRange,
                    onValueChange = { salaryRange = it },
                    label = { Text("Salary Range") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = applyLink,
                    onValueChange = { applyLink = it },
                    label = { Text("Apply Link") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = applyEmail,
                    onValueChange = { applyEmail = it },
                    label = { Text("Apply Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Location Type", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LocationType.entries.forEach { type ->
                        FilterChip(
                            selected = locationType == type,
                            onClick = { locationType = type },
                            label = { Text(type.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Company Type", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompanyType.entries.forEach { type ->
                        FilterChip(
                            selected = companyType == type,
                            onClick = { companyType = type },
                            label = { Text(type.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Job Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.generateCoverLetter(
                                    description, companyName, positionTitle
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Generate Cover Letter")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.saveJob(
                                    companyName = companyName,
                                    positionTitle = positionTitle,
                                    jobCategory = jobCategory,
                                    salaryRange = salaryRange,
                                    deadline = deadline,
                                    applyLink = applyLink,
                                    applyEmail = applyEmail,
                                    locationType = locationType,
                                    companyType = companyType,
                                    description = description
                                )
                                onJobSaved()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Job")
                    }
                }
            }
        }
    }
}

enum class InputMethod {
    LINK,
    TEXT
}