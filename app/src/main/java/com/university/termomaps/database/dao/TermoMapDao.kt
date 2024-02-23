package com.university.termomaps.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.university.termomaps.database.entity.TermoMap
import com.university.termomaps.database.entity.TermoMapWithMarkers
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TermoMapDao : BaseDao<TermoMap>() {

  @Query("SELECT * FROM termo_map")
  abstract override fun getAll(): Flow<List<TermoMap>>

  @Query("DELETE FROM termo_map")
  abstract override suspend fun deleteAll()

  @Query("SELECT * FROM termo_map")
  abstract fun getTermoMapsWithMarkers(): Flow<List<TermoMapWithMarkers>>

  @Query("SELECT * FROM termo_map WHERE id = :id")
  abstract fun getTermoMapWithMarkers(id: Int): Flow<TermoMapWithMarkers>

  @Query("DELETE FROM termo_map WHERE id = :id")
  abstract suspend fun deleteMap(id: Int)
}