package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SkyAnalysisDao {
    @Query("SELECT * FROM sky_analysis_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<SkyAnalysisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(entity: SkyAnalysisEntity): Long

    @Query("DELETE FROM sky_analysis_history WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM sky_analysis_history")
    suspend fun clearAllHistory()
}
