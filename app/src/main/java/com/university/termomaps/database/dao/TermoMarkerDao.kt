package com.university.termomaps.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.university.termomaps.database.entity.TermoMarker
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TermoMarkerDao : BaseDao<TermoMarker>() {

  @Query("SELECT * FROM termo_marker")
  abstract override fun getAll(): Flow<List<TermoMarker>>

  @Query("DELETE FROM termo_marker")
  abstract override suspend fun deleteAll()
}