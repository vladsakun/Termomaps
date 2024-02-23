package com.university.termomaps.data

import com.university.termomaps.database.dao.TermoMarkerDao
import com.university.termomaps.database.entity.marker.TermoMarker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TermoMarkerRepository @Inject constructor(
  private val termoMapDao: TermoMarkerDao,
) {

  val allMarkers: Flow<List<TermoMarker>> = termoMapDao.getAll()

  fun getMarkerById(id: Long): Flow<TermoMarker> = termoMapDao.getById(id)

  suspend fun updateMarkerDetails(id: Long, name: String, temperatureLoss: Int) =
    withContext(Dispatchers.IO) {
      termoMapDao.updateMarkerDetails(
        id = id,
        name = name,
        temperatureLoss = temperatureLoss,
      )
    }

  suspend fun upsert(marker: TermoMarker) =
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(marker)
    }

  suspend fun upsert(markers: List<TermoMarker>) =
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(markers)
    }

  suspend fun delete(marker: TermoMarker) =
    withContext(Dispatchers.IO) {
      termoMapDao.delete(marker)
    }

  suspend fun delete(markerId: Long) =
    withContext(Dispatchers.IO) {
      termoMapDao.delete(markerId)
    }

  suspend fun deleteAll(markers: List<TermoMarker>) =
    withContext(Dispatchers.IO) {
      termoMapDao.deleteAll(markers)
    }

  suspend fun deleteAll() =
    withContext(Dispatchers.IO) {
      termoMapDao.deleteAll()
    }
}