package com.university.termomaps.data

import com.university.termomaps.database.dao.TermoMarkerDao
import com.university.termomaps.database.entity.TermoMarker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TermoMarkerRepository @Inject constructor(
  private val termoMapDao: TermoMarkerDao,
) {

  val allMarkers: Flow<List<TermoMarker>> = termoMapDao.getAll()

  suspend fun upsert(marker: TermoMarker) {
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(marker)
    }
  }

  suspend fun upsert(markers: List<TermoMarker>) {
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(markers)
    }
  }

  suspend fun delete(marker: TermoMarker) {
    termoMapDao.delete(marker)
  }

  suspend fun deleteAll(markers: List<TermoMarker>) {
    termoMapDao.deleteAll(markers)
  }

  suspend fun deleteAll() {
    termoMapDao.deleteAll()
  }
}