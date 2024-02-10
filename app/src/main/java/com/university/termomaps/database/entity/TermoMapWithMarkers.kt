package com.university.termomaps.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TermoMapWithMarkers(
  @Embedded
  val termoMap: TermoMap,
  @Relation(parentColumn = "id", entityColumn = "termoMapId", entity = TermoMarker::class)
  val markers: List<TermoMarker>,
)