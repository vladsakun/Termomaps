package com.university.termomaps.data

import com.university.termomaps.database.entity.TermoMapWithMarkers
import javax.inject.Inject

class UpsertTermoMapWithMarkersUseCase @Inject constructor(
  private val termoMapRepository: TermoMapRepository,
  private val termoMarkerRepository: TermoMarkerRepository,
) {

  suspend operator fun invoke(termoMapWithMarkers: TermoMapWithMarkers) {
    termoMapRepository.upsert(termoMapWithMarkers.termoMap)
    termoMarkerRepository.upsert(termoMapWithMarkers.markers)
  }
}