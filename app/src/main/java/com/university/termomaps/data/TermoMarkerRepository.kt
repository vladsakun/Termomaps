package com.university.termomaps.data

import com.university.termomaps.database.dao.TermoMarkerDao
import com.university.termomaps.database.entity.marker.TermoMarker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Репозиторій для роботи з маркерами
 */
class TermoMarkerRepository @Inject constructor(
  private val termoMapDao: TermoMarkerDao,
) {

  /**
   * Отримати маркер по id
   *
   * @param id - ідентифікатор маркера
   * @return
   */
  fun getMarkerById(id: Long): Flow<TermoMarker> = termoMapDao.getById(id)

  /**
   * Оновити деталі маркера
   *
   * @param id - ідентифікатор маркера
   * @param name - назва маркера
   * @param temperatureLoss - втрати температури
   */
  suspend fun updateMarkerDetails(id: Long, name: String, temperatureLoss: Int) =
    withContext(Dispatchers.IO) {
      termoMapDao.updateMarkerDetails(
        id = id,
        name = name,
        temperatureLoss = temperatureLoss,
      )
    }

  /**
   * Оновити маркер
   *
   * @param marker - маркер
   */
  suspend fun upsert(marker: TermoMarker) =
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(marker)
    }

  /**
   * Оновити список маркерів
   *
   * @param markers - список маркерів
   */
  suspend fun upsert(markers: List<TermoMarker>) =
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(markers)
    }

  /**
   * Видалити маркер по id
   *
   * @param markerId - ідентифікатор маркера
   */
  suspend fun delete(markerId: Long) =
    withContext(Dispatchers.IO) {
      termoMapDao.delete(markerId)
    }
}