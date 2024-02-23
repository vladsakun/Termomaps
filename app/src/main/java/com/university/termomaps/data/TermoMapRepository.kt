package com.university.termomaps.data

import com.university.termomaps.database.dao.TermoMapDao
import com.university.termomaps.database.entity.TermoMap
import com.university.termomaps.database.entity.TermoMapWithMarkers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class TermoMapRepository @Inject constructor(
  private val termoMapDao: TermoMapDao,
) {

  val termoMapsWithMarkers: Flow<List<TermoMapWithMarkers>> =
    termoMapDao.getTermoMapsWithMarkers()

  fun getTermoMapWithMarkers(id: Int): Flow<TermoMapWithMarkers> =
    termoMapDao.getTermoMapWithMarkers(id)

  suspend fun createMap(name: String, description: String): Int =
    withContext(Dispatchers.IO) {
      val id = UUID.randomUUID().hashCode()
      termoMapDao.upsert(
        TermoMap(
          name = name,
          description = description,
          id = id,
        )
      )

      id
    }

  suspend fun upsert(termoMap: TermoMap) =
    withContext(Dispatchers.IO) {
      termoMapDao.upsert(termoMap)
    }

  suspend fun deleteAll() {
    termoMapDao.deleteAll()
  }

  suspend fun deleteMap(mapId: Int) =
    withContext(Dispatchers.IO) {
      termoMapDao.deleteMap(mapId)
    }
}