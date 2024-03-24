package com.university.termomaps.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.university.termomaps.database.entity.marker.TermoMarker
import kotlinx.coroutines.flow.Flow

/**
 * Dao для роботи з маркерами
 */
@Dao
abstract class TermoMarkerDao : BaseDao<TermoMarker>() {

  // Отримати список всіх маркерів
  @Query("SELECT * FROM termo_marker")
  abstract override fun getAll(): Flow<List<TermoMarker>>

  // Отримати маркер по id
  @Query("SELECT * FROM termo_marker WHERE id = :id")
  abstract fun getById(id: Long): Flow<TermoMarker>

  // Оновити деталі маркера
  @Query("UPDATE termo_marker SET name = :name, temperatureLoss = :temperatureLoss WHERE id = :id")
  abstract suspend fun updateMarkerDetails(id: Long, name: String, temperatureLoss: Int)

  // Видалити маркер по id
  @Query("DELETE FROM termo_marker WHERE id = :id")
  abstract suspend fun delete(id: Long)
}