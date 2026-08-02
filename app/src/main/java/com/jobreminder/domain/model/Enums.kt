package com.jobreminder.domain.model

enum class JobCategory(val displayName: String) {
    AI_ENGINEER("AI Engineer"),
    ML_ENGINEER("ML Engineer"),
    DATA_SCIENCE("Data Science"),
    FULL_STACK("Full-Stack"),
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    DEVOPS("DevOps"),
    MOBILE("Mobile"),
    CLOUD("Cloud"),
    SECURITY("Security"),
    OTHER("Other");

    companion object {
        fun fromString(value: String): JobCategory {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: OTHER
        }
    }
}

enum class JobStatus(val displayName: String) {
    DRAFT("Draft"),
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    OFFER_RECEIVED("Offer Received"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted"),
    WITHDRAWN("Withdrawn");

    companion object {
        fun fromString(value: String): JobStatus {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: DRAFT
        }
    }
}

enum class LocationType(val displayName: String) {
    REMOTE("Remote"),
    ONSITE("Onsite"),
    HYBRID("Hybrid"),
    UNKNOWN("Unknown");

    companion object {
        fun fromString(value: String): LocationType {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: UNKNOWN
        }
    }
}

enum class CompanyType(val displayName: String) {
    MULTINATIONAL("Multinational"),
    STARTUP("Startup"),
    NATIVE("Native"),
    UNKNOWN("Unknown");

    companion object {
        fun fromString(value: String): CompanyType {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: UNKNOWN
        }
    }
}

enum class ReminderType(val displayName: String) {
    FOLLOW_UP("Follow Up"),
    DEADLINE("Deadline"),
    INTERVIEW_PREP("Interview Prep"),
    THANK_YOU_NOTE("Thank You Note");

    companion object {
        fun fromString(value: String): ReminderType {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: DEADLINE
        }
    }
}

enum class InterviewType(val displayName: String) {
    PHONE("Phone"),
    VIDEO("Video"),
    IN_PERSON("In Person"),
    PANEL("Panel");

    companion object {
        fun fromString(value: String): InterviewType {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: PHONE
        }
    }
}