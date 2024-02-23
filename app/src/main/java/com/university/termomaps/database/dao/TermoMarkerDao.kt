package com.university.termomaps.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.university.termomaps.database.entity.marker.TermoMarker
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TermoMarkerDao : BaseDao<TermoMarker>() {

  @Query("SELECT * FROM termo_marker")
  abstract override fun getAll(): Flow<List<TermoMarker>>

  @Query("SELECT * FROM termo_marker WHERE id = :id")
  abstract fun getById(id: Long): Flow<TermoMarker>

  @Query("UPDATE termo_marker SET name = :name, temperatureLoss = :temperatureLoss WHERE id = :id")
  abstract suspend fun updateMarkerDetails(id: Long, name: String, temperatureLoss: Int)

  @Query("DELETE FROM termo_marker")
  abstract override suspend fun deleteAll()

  @Query("DELETE FROM termo_marker WHERE id = :id")
  abstract suspend fun delete(id: Long)
}