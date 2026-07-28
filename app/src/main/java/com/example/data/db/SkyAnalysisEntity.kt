package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sky_analysis_history")
data class SkyAnalysisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imagePath: String, // URI or cached file path
    val timestamp: Long = System.currentTimeMillis(),
    val rainProbability: Int, // 0 to 100
    val willRainSoon: Boolean,
    val rainLevel: String, // "高风险/强降雨", "中风险/微雨", "低风险/晴朗无雨"
    val cloudType: String, // e.g. "积雨云 (Cumulonimbus)"
    val rainTimeframe: String, // e.g. "预计 15-30 分钟内"
    val skyFeaturesJson: String, // Pipe-separated or JSON list of features
    val advice: String, // e.g. "建议带伞，收回户外晾衣"
    val detailedAnalysis: String
)
