package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.GeminiService
import com.example.data.SampleSkyData
import com.example.data.SampleSkyItem
import com.example.data.SkyAnalysisResult
import com.example.data.SkyRepository
import com.example.data.db.SkyAnalysisEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AnalysisUiState {
    object Idle : AnalysisUiState()
    object Analyzing : AnalysisUiState()
    data class Success(val result: SkyAnalysisResult, val imageUri: Uri) : AnalysisUiState()
    data class Error(val message: String) : AnalysisUiState()
}

class SkyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val geminiService = GeminiService(application)
    private val repository = SkyRepository(geminiService, db.skyAnalysisDao())

    private val _selectedUri = MutableStateFlow<Uri?>(null)
    val selectedUri: StateFlow<Uri?> = _selectedUri.asStateFlow()

    private val _uiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    val historyState: StateFlow<List<SkyAnalysisEntity>> = repository.historyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _samples = MutableStateFlow<List<SampleSkyItem>>(emptyList())
    val samples: StateFlow<List<SampleSkyItem>> = _samples.asStateFlow()

    private val _showGuide = MutableStateFlow(false)
    val showGuide: StateFlow<Boolean> = _showGuide.asStateFlow()

    private val _showHistory = MutableStateFlow(false)
    val showHistory: StateFlow<Boolean> = _showHistory.asStateFlow()

    init {
        loadSampleSkies()
    }

    fun loadSampleSkies() {
        viewModelScope.launch {
            _samples.value = SampleSkyData.getSampleSkyItems(getApplication())
        }
    }

    fun selectAndAnalyze(uri: Uri) {
        _selectedUri.value = uri
        _uiState.value = AnalysisUiState.Analyzing

        viewModelScope.launch {
            try {
                val result = repository.analyzeAndSave(uri)
                _uiState.value = AnalysisUiState.Success(result, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = AnalysisUiState.Error(e.message ?: "识别失败，请重新拍照或选图")
            }
        }
    }

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteHistoryRecord(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun toggleGuide(show: Boolean) {
        _showGuide.value = show
    }

    fun toggleHistory(show: Boolean) {
        _showHistory.value = show
    }

    fun resetAnalysis() {
        _selectedUri.value = null
        _uiState.value = AnalysisUiState.Idle
    }
}
