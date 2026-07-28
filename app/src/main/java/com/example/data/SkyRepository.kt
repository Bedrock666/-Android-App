package com.example.data

import android.net.Uri
import com.example.data.db.SkyAnalysisDao
import com.example.data.db.SkyAnalysisEntity
import kotlinx.coroutines.flow.Flow

class SkyRepository(
    private val geminiService: GeminiService,
    private val skyAnalysisDao: SkyAnalysisDao
) {

    val historyFlow: Flow<List<SkyAnalysisEntity>> = skyAnalysisDao.getAllHistory()

    suspend fun analyzeAndSave(imageUri: Uri): SkyAnalysisResult {
        val result = geminiService.analyzeSkyImage(imageUri)

        // Save into local Room database
        val entity = SkyAnalysisEntity(
            imagePath = imageUri.toString(),
            timestamp = System.currentTimeMillis(),
            rainProbability = result.rainProbability,
            willRainSoon = result.willRainSoon,
            rainLevel = result.rainLevel,
            cloudType = result.cloudType,
            rainTimeframe = result.rainTimeframe,
            skyFeaturesJson = result.skyFeatures.joinToString("|"),
            advice = result.advice,
            detailedAnalysis = result.detailedAnalysis
        )
        skyAnalysisDao.insertRecord(entity)

        return result
    }

    suspend fun deleteHistoryRecord(id: Long) {
        skyAnalysisDao.deleteRecordById(id)
    }

    suspend fun clearHistory() {
        skyAnalysisDao.clearAllHistory()
    }
}
