package com.university.termomaps.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.university.termomaps.database.entity.marker.TermoMarker
import kotlinx.serialization.Serializable

@Serializable
data class TermoMapWithMarkers(
  @Embedded
  val termoMap: TermoMap,
  @Relation(parentColumn = "id", entityColumn = "termoMapId", entity = TermoMarker::class)
  val markers: List<TermoMarker>,
)