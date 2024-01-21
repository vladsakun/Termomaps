package com.university.termomaps.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TermoMarkerDao {

  @Query("SELECT * FROM termo_marker")
  fun getAll(): Flow<List<TermoMarker>>

  @Upsert
  suspend fun upsert(termoMarkers: TermoMarker)

  @Upsert
  suspend fun upsert(termoMarkers: List<TermoMarker>)

  @Delete
  suspend fun delete(termoMarker: TermoMarker)

  @Delete
  suspend fun deleteAll(termoMarkers: List<TermoMarker>)

  @Query("DELETE FROM termo_marker")
  suspend fun deleteAll()
}