package com.jobreminder.data.remote.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.jobreminder.data.remote.model.FirestoreJobApplication
import com.jobreminder.data.remote.model.FirestoreResume
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getUserDocument(userId: String) = db.collection("users").document(userId)

    // Job Applications
    suspend fun saveJob(userId: String, job: FirestoreJobApplication): Result<Unit> {
        return try {
            getUserDocument(userId)
                .collection("jobs")
                .document(job.id)
                .set(job)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteJob(userId: String, jobId: String): Result<Unit> {
        return try {
            getUserDocument(userId)
                .collection("jobs")
                .document(jobId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobs(userId: String): Result<List<FirestoreJobApplication>> {
        return try {
            val snapshot = getUserDocument(userId)
                .collection("jobs")
                .get()
                .await()
            val jobs = snapshot.toObjects(FirestoreJobApplication::class.java)
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Resumes
    suspend fun saveResume(userId: String, resume: FirestoreResume): Result<Unit> {
        return try {
            getUserDocument(userId)
                .collection("resumes")
                .document(resume.id)
                .set(resume)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteResume(userId: String, resumeId: String): Result<Unit> {
        return try {
            getUserDocument(userId)
                .collection("resumes")
                .document(resumeId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getResumes(userId: String): Result<List<FirestoreResume>> {
        return try {
            val snapshot = getUserDocument(userId)
                .collection("resumes")
                .get()
                .await()
            val resumes = snapshot.toObjects(FirestoreResume::class.java)
            Result.success(resumes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}