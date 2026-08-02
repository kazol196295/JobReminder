package com.jobreminder.util

import java.io.FileInputStream
import java.util.Properties

object ApiConfig {
    // Mimo Auto Model API Configuration
    const val MIMO_API_URL = "https://api.mimo.com/v1/chat/completions"
    const val MIMO_MODEL = "mimo-auto"  // Mimo auto model
    
    // Load API key from local.properties (not committed to git)
    fun getMimoApiKey(): String {
        return try {
            val properties = Properties()
            val localPropertiesFile = java.io.File("local.properties")
            if (localPropertiesFile.exists()) {
                FileInputStream(localPropertiesFile).use { 
                    properties.load(it)
                }
                properties.getProperty("MIMO_API_KEY", "")
            } else {
                System.getenv("MIMO_API_KEY") ?: ""
            }
        } catch (e: Exception) {
            System.getenv("MIMO_API_KEY") ?: ""
        }
    }
}