# Product Requirements Document (PRD)
## JobReminder App

### 1. Introduction
JobReminder is an AI-powered Android application that helps job seekers instantly parse job postings from links or copied text, automatically extract key details, track applications, and never miss deadlines.

### 2. Problem Statement
Job seekers face these challenges:
- Manually entering job details from multiple sources is tedious
- Missing application deadlines due to poor tracking
- Losing track of which jobs they applied to
- Difficulty organizing jobs by type, salary, remote/onsite status
- Forgetting to follow up or prepare for interviews

### 3. Target Users
- Active job seekers applying to multiple positions daily
- Career changers managing complex job searches
- Recent graduates entering the job market
- Freelancers/contractors juggling multiple opportunities

### 4. Core Features

#### 4.1 Smart Job Input (Primary Feature)
Users can add jobs in two ways:

**Option A: Link Input**
- User pastes a job posting URL (LinkedIn, Indeed, company career page, etc.)
- App fetches the webpage content
- AI extracts all relevant details automatically

**Option B: Copied Text Input**
- User copies job description text from any source
- User pastes it into the app
- AI parses and extracts structured data

**Extracted Data Fields:**
| Field | Description | Example |
|-------|-------------|---------|
| Company Name | Name of hiring company | Google, Microsoft, StartupXYZ |
| Position Title | Job role/title | Senior ML Engineer |
| Job Stack/Category | Tech stack or role type | AI Engineer, ML Engineer, Full-Stack, Backend, Frontend, DevOps, Data Science, Mobile |
| Salary Range | Compensation if listed | $120k - $180k/year |
| Submission Deadline | Last date to apply | 2026-07-15 |
| Apply Link/Email | How to apply | https://careers.google.com/apply or hr@company.com |
| Location Type | Remote, Onsite, or Hybrid | Remote |
| Company Type | Multinational, Startup, or Native | Multinational |
| Job Description | Full description text | (stored for reference) |
| Requirements | Skills/experience needed | Python, TensorFlow, 5+ years |

#### 4.2 AI Cover Letter Generator
- Optional feature when adding a job
- Uses user's saved resume context (not just profile info)
- AI generates a tailored cover letter based on:
  - Resume: skills, experience, education
  - Job description and requirements
  - Company type and culture
- User can edit/regenerate before saving
- Multiple resume support for different job types

#### 4.3 Smart Deadline Notifications
- Automatic notification scheduling when deadline is detected
- Notification tiers:
  - 7 days before deadline
  - 3 days before deadline
  - 1 day before deadline
  - On deadline day (morning)
- Custom notification preferences
- Urgent deadline alerts for jobs closing soon

#### 4.4 Job Application Tracking
- Categorize applications by status:
  - Draft
  - Applied
  - Under Review
  - Interview Scheduled
  - Offer Received
  - Rejected
  - Accepted
  - Withdrawn
- Filter and sort by:
  - Job stack/category
  - Salary range
  - Location type (remote/onsite/hybrid)
  - Company type
  - Deadline proximity
  - Status

#### 4.5 Reminder System
- Set custom reminders for:
  - Follow-up dates
  - Interview preparation
  - Thank-you note sending
- Notification preferences:
  - Push notifications
  - In-app alerts

#### 4.6 Resume Management
- Create and manage multiple resumes
- Each resume includes:
  - Personal info (name, email, phone)
  - Professional summary
  - Skills list
  - Work experience
  - Education
- Set default resume for cover letter generation
- Different resumes for different job types (AI, Full-Stack, etc.)

#### 4.7 Interview Management
- Schedule interviews with:
  - Date and time
  - Interview type (phone, video, in-person)
  - Interviewer details
  - Location/link for interview
- Preparation checklist
- Post-interview notes and feedback

#### 4.7 Document Management
- Store and organize:
  - Resume versions
  - Generated cover letters
  - Portfolio links
- Associate documents with specific applications

#### 4.8 Calendar Integration
- Sync with Google Calendar
- Visual timeline of applications and interviews
- Deadline highlighting

#### 4.9 Analytics & Reporting
- Application statistics:
  - Total applications by category
  - Response rate
  - Interview conversion rate
  - Salary range distribution
- Progress visualization
- Export functionality

### 5. User Stories

#### 5.1 Smart Input
- As a user, I want to paste a job link and have all details extracted automatically
- As a user, I want to paste copied job text and get structured data
- As a user, I want to see extracted fields before saving for verification
- As a user, I want to edit any extracted field if the AI got it wrong

#### 5.2 Job Tracking
- As a user, I want to filter jobs by salary range to compare opportunities
- As a user, I want to see all remote jobs in one view
- As a user, I want to sort jobs by deadline urgency
- As a user, I want to see jobs grouped by company type

#### 5.3 Notifications
- As a user, I want automatic reminders before application deadlines
- As a user, I want to know which jobs are closing soon
- As a user, I want to customize when I receive deadline notifications

#### 5.4 Cover Letter
- As a user, I want AI to generate a cover letter using my resume context
- As a user, I want the cover letter to match my experience to job requirements
- As a user, I want to edit the generated cover letter before saving
- As a user, I want to store cover letters with their respective applications

#### 5.5 Resume Management
- As a user, I want to create and save my resume in the app
- As a user, I want to create multiple resumes for different job types
- As a user, I want to set a default resume for quick cover letter generation
- As a user, I want to edit my resume and have cover letters reflect updates

#### 5.6 Multi-Device Sync
- As a user, I want my data synced across all my devices
- As a user, I want to sign in with Google for seamless access
- As a user, I want offline access to my recent applications

### 6. Input Flow

```
┌─────────────────────┐
│   User Opens App    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Tap "+" Add Job    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Choose Input Method │
└──────────┬──────────┘
           │
     ┌─────┴─────┐
     ▼           ▼
┌─────────┐ ┌─────────┐
│  Link   │ │  Text   │
│  Input  │ │  Paste  │
└────┬────┘ └────┬────┘
     │           │
     ▼           ▼
┌─────────────────────┐
│  AI Parses Content  │
│  (shows loading)    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Edit/Verify Fields │
│  - Company Name     │
│  - Position         │
│  - Stack Category   │
│  - Salary Range     │
│  - Deadline         │
│  - Apply Link       │
│  - Location Type    │
│  - Company Type     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Optional: Generate  │
│ Cover Letter (AI)   │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Save Job Application│
│  Auto-set Deadline  │
│  Notifications      │
└─────────────────────┘
```

### 7. Technical Requirements

#### 7.1 Platform Support
- Android application (API 26+)
- Cross-device sync via Firebase

#### 7.2 AI Integration
- Text extraction from URLs (web scraping)
- Natural Language Processing for text parsing
- Cover letter generation
- Classification of job categories

#### 7.3 Data Security
- Secure user authentication (Google Sign-In)
- Data encryption at rest and in transit
- Regular backups via Firestore
- GDPR compliance

#### 7.4 Performance
- Fast parsing (<5 seconds)
- Offline capability for viewing saved jobs
- Real-time sync when online

### 8. Non-Functional Requirements
- Accessibility compliance
- Dark/light mode preference
- Customizable notification settings
- Multiple language support (future)

### 9. Success Metrics
- User retention rate (>40% after 30 days)
- Jobs parsed per user (>15/month)
- Parsing accuracy (>90%)
- Reminder completion rate (>70%)
- User satisfaction score (>4.5/5)

### 10. Future Enhancements
- Resume parsing and optimization
- Job recommendations based on profile
- Salary negotiation tools
- Integration with more job boards
- Team/company tracking
- Application timeline visualization

### 11. Timeline & Milestones
- Phase 1: Smart job input + basic tracking (MVP)
- Phase 2: Cover letter generation + notifications
- Phase 3: Interview management + calendar
- Phase 4: Analytics + polish

### 12. Risks & Mitigations
- **Risk**: Website scraping blocked → **Mitigation**: Fallback to text input, use multiple parsing strategies
- **Risk**: AI parsing errors → **Mitigation**: User verification step, feedback loop for improvement
- **Risk**: Rate limiting on AI API → **Mitigation**: Caching, batch processing, local fallback models
- **Risk**: Privacy concerns with AI → **Mitigation**: Clear data policy, user consent, local processing option