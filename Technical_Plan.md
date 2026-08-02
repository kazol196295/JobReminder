# JobReminder Android App - Technical Plan

## Tech Stack

### Core
- **Language**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture
- **Build System**: Gradle (Kotlin DSL)
- **Backend**: Firebase (Auth + Firestore)
- **AI/ML**: OpenAI API + Local parsing

### Dependencies

#### UI & Design
- `androidx.compose.material3:material3` - Material Design 3 components
- `androidx.compose.ui:ui` - Core Compose UI
- `androidx.activity:activity-compose` - Compose activity integration
- `androidx.navigation:navigation-compose` - Screen navigation
- `com.google.accompanist:accompanist-systemuicontroller` - System UI control

#### Database & Storage
- `androidx.room:room-runtime` - Local database
- `androidx.room:room-ktx` - Kotlin extensions for Room
- `androidx.room:room-compiler` - Room annotation processor
- `androidx.datastore:datastore-preferences` - User preferences

#### Dependency Injection
- `io.insert-koin:koin-android` - Dependency injection
- `io.insert-koin:koin-androidx-compose` - Koin Compose integration

#### Date & Time
- `com.jakewharton.threetenabp:threetenabp` - ThreeTenABP for date/time handling

#### Authentication & Cloud Sync
- `com.google.firebase:firebase-auth-ktx` - Firebase Authentication
- `com.google.firebase:firebase-firestore-ktx` - Cloud Firestore database
- `com.google.android.gms:play-services-auth` - Google Sign-In
- `com.google.firebase:firebase-bom` - Firebase BOM for version management

#### AI & Web Scraping (Xiaomi MiMo)
- `com.squareup.okhttp3:okhttp` - HTTP client for URL fetching
- `org.jsoup:jsoup` - HTML parsing and content extraction
- `org.json:json` - JSON parsing
- Xiaomi MiMo API (MiMo-7B-RL model) for job parsing and cover letter generation

#### Notifications
- `androidx.work:work-runtime-ktx` - WorkManager for scheduling
- `com.google.firebase:firebase-messaging-ktx` - Firebase Cloud Messaging

#### Testing
- `junit:junit` - Unit testing
- `androidx.compose.ui:ui-test-junit4` - Compose testing
- `io.mockk:mockk` - Mocking library

## Project Structure

```
app/src/main/java/com/jobreminder/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAOs
│   │   ├── database/      # Room Database
│   │   └── entity/        # Database entities
│   ├── remote/
│   │   ├── firebase/
│   │   │   ├── auth/      # Firebase Auth
│   │   │   └── firestore/ # Firestore operations
│   │   └── model/         # Remote data models
│   └── repository/        # Repository implementations
├── di/                    # Dependency injection modules
├── domain/
│   ├── model/             # Domain models
│   ├── repository/        # Repository interfaces
│   └── usecase/           # Use cases
├── ai/
│   ├── parser/            # Job content parsers
│   │   ├── UrlParser.kt       # URL content fetcher
│   │   ├── TextParser.kt      # Text content parser
│   │   └── JobExtractor.kt    # AI-powered extraction
│   ├── coverletter/       # Cover letter generator
│   │   └── CoverLetterGenerator.kt
│   └── model/             # AI models
│       └── ParsedJobData.kt
├── ui/
│   ├── auth/              # Authentication screens
│   │   └── LoginScreen.kt
│   ├── screens/
│   │   ├── home/          # Home dashboard
│   │   ├── addjob/        # Smart job input
│   │   ├── jobdetail/     # Job detail view
│   │   ├── resume/        # Resume management
│   │   ├── interviews/    # Interviews list
│   │   ├── calendar/      # Calendar view
│   │   └── settings/      # Settings screen
│   ├── components/        # Reusable UI components
│   ├── theme/             # Material 3 theme
│   └── navigation/        # Navigation setup
├── notification/          # Notification handling
│   ├── ReminderWorker.kt
│   └── NotificationHelper.kt
├── util/                  # Utility classes
└── JobReminderApp.kt      # Application class
```

## AI Architecture

### Job Parsing Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    USER INPUT                               │
│  ┌─────────────────┐    ┌─────────────────┐                │
│  │   URL Link      │    │   Copied Text   │                │
│  └────────┬────────┘    └────────┬────────┘                │
│           │                      │                          │
│           ▼                      ▼                          │
│  ┌─────────────────┐    ┌─────────────────┐                │
│  │  OkHttp Fetch   │    │  Direct Input   │                │
│  └────────┬────────┘    └────────┬────────┘                │
│           │                      │                          │
│           ▼                      │                          │
│  ┌─────────────────┐            │                          │
│  │  Jsoup Parse    │            │                          │
│  │  HTML → Text    │            │                          │
│  └────────┬────────┘            │                          │
│           │                      │                          │
│           └──────────┬───────────┘                          │
│                      ▼                                      │
│  ┌─────────────────────────────────────┐                   │
│  │         RAW JOB CONTENT            │                   │
│  └──────────────────┬──────────────────┘                   │
│                     │                                       │
│                     ▼                                       │
│  ┌─────────────────────────────────────┐                   │
│  │    AI PARSING (OpenAI API)          │                   │
│  │    Extract structured fields:       │                   │
│  │    - company_name                    │                   │
│  │    - position_title                  │                   │
│  │    - job_stack (AI/ML/Fullstack)     │                   │
│  │    - salary_range                    │                   │
│  │    - deadline                        │                   │
│  │    - apply_link_or_email             │                   │
│  │    - location_type (remote/onsite)   │                   │
│  │    - company_type (MNC/native)       │                   │
│  │    - requirements                    │                   │
│  └──────────────────┬──────────────────┘                   │
│                     │                                       │
│                     ▼                                       │
│  ┌─────────────────────────────────────┐                   │
│  │      PARSED JOB DATA (UI Model)     │                   │
│  │      User can edit/verify fields    │                   │
│  └─────────────────────────────────────┘                   │
└─────────────────────────────────────────────────────────────┘
```

### Cover Letter Generation Flow

```
┌─────────────────────────────────────────┐
│  User taps "Generate Cover Letter"      │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│  Gather context:                        │
│  - Job description                      │
│  - Company name & type                  │
│  - User profile/resume                  │
│  - Job requirements                     │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│  OpenAI API Call:                       │
│  "Generate a cover letter for           │
│   [position] at [company]..."           │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│  Generated Cover Letter                 │
│  User can edit/regenerate/save          │
└─────────────────────────────────────────┘
```

## Screens & Features

### 0. Authentication Screen
- Google Sign-In button
- Auto-login if already authenticated

### 1. Home Screen (Dashboard)
- List of active job applications
- Filter by:
  - Job category (AI, ML, Full-Stack, Backend, etc.)
  - Salary range
  - Location type (Remote/Onsite/Hybrid)
  - Company type (MNC/Startup/Native)
  - Status
  - Deadline proximity
- Search functionality
- FAB to add new job
- Quick stats (total applications, upcoming deadlines)
- "Closing Soon" section for jobs with near deadlines

### 2. Smart Job Input Screen
- **Input Method Toggle:**
  - [Link] - Paste URL
  - [Text] - Paste copied text
- **Input Field:**
  - Large text field for URL or pasted text
  - "Parse" button
- **Loading State:**
  - Progress indicator with "Analyzing job posting..."
- **Parsed Results (Editable):**
  - Company Name (text field)
  - Position Title (text field)
  - Job Stack/Category (dropdown: AI Engineer, ML Engineer, Full-Stack, Backend, Frontend, DevOps, Data Science, Mobile, Other)
  - Salary Range (text field)
  - Submission Deadline (date picker)
  - Apply Link/Email (text field with link icon)
  - Location Type (toggle: Remote / Onsite / Hybrid)
  - Company Type (toggle: Multinational / Startup / Native)
  - Job Description (expandable text)
  - Requirements (tags/chips)
- **Actions:**
  - [Generate Cover Letter] - AI cover letter generation
  - [Save Job] - Save to database

### 3. Job Detail Screen
- Full job information
- Status timeline
- Associated documents (cover letter, resume)
- Quick actions:
  - Apply Now (opens apply link)
  - Set Reminder
  - Generate Cover Letter
  - Edit
  - Delete/Archive

### 4. Interviews Screen
- Upcoming interviews list
- Interview details (date, time, type, location)
- Preparation checklist
- Post-interview notes

### 5. Resume Management Screen
- Add/Edit resume with:
  - Full name, email, phone
  - Professional summary
  - Skills (add/remove tags)
  - Work experience (title, company, duration, description)
  - Education (degree, institution, year)
- Set default resume
- Multiple resume support for different job types
- Import from file (future)

### 6. Calendar View
- Monthly calendar showing deadlines
- Color-coded by job category
- Tap to view details

### 6. Settings Screen
- Account management (sign out, delete account)
- Sync status indicator
- Auto-sync toggle
- Notification preferences (deadline reminders timing)
- Theme toggle (Light/Dark)
- Data export/backup
- AI API key configuration (for advanced users)
- About section

## Data Models

### Local (Room)

#### JobApplication
```kotlin
@Entity(tableName = "job_applications")
data class JobApplication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val companyName: String,
    val positionTitle: String,
    val jobCategory: JobCategory,
    val applicationDate: LocalDate,
    val status: JobStatus,
    val description: String? = null,
    val requirements: String? = null,  // JSON array of requirements
    val salaryRange: String? = null,
    val locationType: LocationType,
    val companyType: CompanyType,
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val deadline: LocalDate? = null,
    val coverLetter: String? = null,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
```

#### Reminder
```kotlin
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val title: String,
    val reminderDate: LocalDateTime,
    val isCompleted: Boolean = false,
    val type: ReminderType
)
```

#### Interview
```kotlin
@Entity(tableName = "interviews")
data class Interview(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val jobId: Long,
    val interviewDate: LocalDateTime,
    val type: InterviewType,
    val location: String? = null,
    val interviewerName: String? = null,
    val notes: String? = null
)
```

#### Resume
```kotlin
@Entity(tableName = "resumes")
data class Resume(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val summary: String? = null,
    val skills: String,  // JSON array of skills
    val experience: String,  // JSON array of experience objects
    val education: String,  // JSON array of education objects
    val isDefault: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class WorkExperience(
    val title: String,
    val company: String,
    val duration: String,
    val description: String
)

data class Education(
    val degree: String,
    val institution: String,
    val year: String
)
```

### Remote (Firestore)

#### FirestoreJobApplication
```kotlin
data class FirestoreJobApplication(
    val id: String = "",
    val companyName: String = "",
    val positionTitle: String = "",
    val jobCategory: String = "",
    val applicationDate: Timestamp? = null,
    val status: String = "",
    val description: String? = null,
    val requirements: String? = null,
    val salaryRange: String? = null,
    val locationType: String = "",
    val companyType: String = "",
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val deadline: Timestamp? = null,
    val coverLetter: String? = null,
    val notes: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
```

#### FirestoreResume
```kotlin
data class FirestoreResume(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String? = null,
    val summary: String? = null,
    val skills: List<String> = emptyList(),
    val experience: List<WorkExperience> = emptyList(),
    val education: List<Education> = emptyList(),
    val isDefault: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
```

### AI Response Model

#### ParsedJobData
```kotlin
data class ParsedJobData(
    val companyName: String = "",
    val positionTitle: String = "",
    val jobCategory: JobCategory = JobCategory.OTHER,
    val salaryRange: String? = null,
    val deadline: String? = null,  // ISO date string
    val applyLink: String? = null,
    val applyEmail: String? = null,
    val locationType: LocationType = LocationType.UNKNOWN,
    val companyType: CompanyType = CompanyType.UNKNOWN,
    val description: String = "",
    val requirements: List<String> = emptyList(),
    val confidence: Float = 0f  // AI confidence score
)
```

### Enums
```kotlin
enum class JobCategory {
    AI_ENGINEER, ML_ENGINEER, DATA_SCIENCE,
    FULL_STACK, BACKEND, FRONTEND,
    DEVOPS, MOBILE, CLOUD, SECURITY, OTHER
}

enum class JobStatus {
    DRAFT, APPLIED, UNDER_REVIEW, INTERVIEW_SCHEDULED,
    OFFER_RECEIVED, REJECTED, ACCEPTED, WITHDRAWN
}

enum class LocationType {
    REMOTE, ONSITE, HYBRID, UNKNOWN
}

enum class CompanyType {
    MULTINATIONAL, STARTUP, NATIVE, UNKNOWN
}

enum class ReminderType {
    FOLLOW_UP, DEADLINE, INTERVIEW_PREP, THANK_YOU_NOTE
}

enum class InterviewType {
    PHONE, VIDEO, IN_PERSON, PANEL
}
```

## AI Parsing Implementation

### UrlParser.kt
```kotlin
class UrlParser(private val client: OkHttpClient) {
    suspend fun fetchContent(url: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val html = response.body?.string() ?: ""
            parseHtml(html)
        }
    }

    private fun parseHtml(html: String): String {
        val doc = Jsoup.parse(html)
        // Remove scripts, styles, nav, footer
        doc.select("script, style, nav, footer, header").remove()
        // Get main content
        val main = doc.select("main, article, .job-description, .content").first()
        return main?.text() ?: doc.body()?.text() ?: ""
    }
}
```

### JobExtractor.kt
```kotlin
class JobExtractor(private val openAiClient: OpenAI) {
    suspend fun extractJobData(content: String): ParsedJobData {
        val prompt = """
            Extract job posting details from this text. Return JSON with:
            - company_name: string
            - position_title: string
            - job_category: one of [AI_ENGINEER, ML_ENGINEER, DATA_SCIENCE, FULL_STACK, 
              BACKEND, FRONTEND, DEVOPS, MOBILE, CLOUD, SECURITY, OTHER]
            - salary_range: string or null
            - deadline: ISO date string or null
            - apply_link: URL string or null
            - apply_email: email string or null
            - location_type: one of [REMOTE, ONSITE, HYBRID]
            - company_type: one of [MULTINATIONAL, STARTUP, NATIVE]
            - description: string
            - requirements: array of strings
            
            Text: $content
        """.trimIndent()

        val response = openAiClient.chatCompletion(
            model = "gpt-4",
            messages = listOf(ChatMessage(Role.User, prompt))
        )
        return parseResponse(response)
    }
}
```

### CoverLetterGenerator.kt
```kotlin
class CoverLetterGenerator(private val openAiClient: OpenAI) {
    suspend fun generate(
        jobDescription: String,
        companyName: String,
        position: String,
        resume: Resume  // Full resume context
    ): String {
        val prompt = """
            Write a professional cover letter for:
            Position: $position
            Company: $companyName
            
            Job Description:
            $jobDescription
            
            My Resume:
            Name: ${resume.fullName}
            Email: ${resume.email}
            Phone: ${resume.phone}
            
            Summary:
            ${resume.summary}
            
            Skills:
            ${resume.skills.joinToString(", ")}
            
            Experience:
            ${resume.experience.joinToString("\n") { 
                "${it.title} at ${it.company} (${it.duration})\n${it.description}" 
            }}
            
            Education:
            ${resume.education.joinToString("\n") { 
                "${it.degree} - ${it.institution} (${it.year})" 
            }}
            
            Requirements:
            - Professional tone
            - Highlight relevant skills from my resume
            - Match my experience to job requirements
            - Show enthusiasm for the role
            - Keep it concise (3-4 paragraphs)
            - Personalize based on company type
        """.trimIndent()

        val response = openAiClient.chatCompletion(
            model = "gpt-4",
            messages = listOf(ChatMessage(Role.User, prompt))
        )
        return response.choices.first().message.content
    }
}
```

## Development Phases

### Phase 1: Foundation (Week 1-2)
- [ ] Set up project with Compose
- [ ] Configure Room database
- [ ] Implement basic navigation
- [ ] Create theme and components

### Phase 2: Firebase & Authentication (Week 3-4)
- [ ] Set up Firebase project
- [ ] Configure Google Sign-In
- [ ] Implement authentication flow
- [ ] Create Firestore data sync layer

### Phase 3: Smart Job Input (Week 5-7)
- [ ] URL fetching with OkHttp
- [ ] HTML parsing with Jsoup
- [ ] AI extraction with OpenAI API
- [ ] Smart input UI with editable fields
- [ ] Job category classification

### Phase 4: Core Features (Week 8-9)
- [ ] Job CRUD operations
- [ ] Home screen with filters
- [ ] Job detail screen
- [ ] Local-remote data synchronization

### Phase 5: Enhanced Features (Week 10-11)
- [ ] Resume management screen
- [ ] Cover letter generation (using resume context)
- [ ] Deadline notifications
- [ ] Interview management
- [ ] Calendar integration

### Phase 6: Polish (Week 12)
- [ ] Settings screen
- [ ] Search and advanced filtering
- [ ] Data export functionality
- [ ] Offline support
- [ ] Testing and bug fixes

## Gradle Dependencies (build.gradle.kts)

```kotlin
dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // Koin
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    
    // AI & Web Scraping
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.aallam.openai:api:3.7.0")
    implementation("com.aallam.openai:api-client-okhttp:3.7.0")
    implementation("org.json:json:20231013")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

## Firebase Setup

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter project name: `jobreminder`
4. Enable Google Analytics (optional)
5. Create project

### 2. Add Android App
1. Click "Add app" → Android
2. Enter package name: `com.jobreminder`
3. Download `google-services.json`
4. Place in `app/` directory

### 3. Enable Authentication
1. Go to Authentication → Sign-in method
2. Enable Google provider
3. Add support email
4. Save

### 4. Create Firestore Database
1. Go to Firestore Database
2. Click "Create database"
3. Start in test mode
4. Choose location closest to users

### 5. Firestore Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 6. Firestore Collection Structure
```
users/
  {userId}/
    jobs/
      {jobId} → FirestoreJobApplication
    reminders/
      {reminderId} → FirestoreReminder
    interviews/
      {interviewId} → FirestoreInterview
    resumes/
      {resumeId} → FirestoreResume
```

## Notes
- Use Kotlin Coroutines and Flow for async operations
- Implement offline-first approach with Room as local cache
- Follow Material Design 3 guidelines
- Use Compose state management (remember, viewModel)
- Firebase provides real-time sync across devices
- Firestore stores user data under `users/{userId}/` collection
- Room database acts as local cache for offline access
- Implement conflict resolution for concurrent edits
- Cache parsed job data to reduce API calls
- Implement retry logic for failed API requests
- Add error handling for unsupported job posting formats