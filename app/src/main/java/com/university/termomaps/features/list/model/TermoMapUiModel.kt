package com.university.termomaps.features.list.model

import com.university.termomaps.R
import com.university.termomaps.database.entity.TermoMap
import com.university.termomaps.database.entity.TermoMapWithMarkers
import com.university.termomaps.database.entity.marker.TermoMarker

data class TermoMapUiModel(
  val id: Int,
  val name: String,
  val description: String,
  val markersAmountText: String,
  val iconResId: Int,
  val markers: List<TermoMarker>,
)

fun asUiModel(termoMapWithMarkers: TermoMapWithMarkers): TermoMapUiModel {
  val markersAmountText = if (termoMapWithMarkers.markers.size > 100) {
    "99+"
  } else {
    termoMapWithMarkers.markers.size.toString()
  }

  val coldMarkersAmount = termoMapWithMarkers.markers.count { it.temperatureLoss <= 20 }
  val warmMarkersAmount = termoMapWithMarkers.markers.size - coldMarkersAmount

  val iconResId = if (coldMarkersAmount > warmMarkersAmount) {
    R.drawable.ic_cold_marker
  } else {
    R.drawable.ic_warm_marker
  }

  return TermoMapUiModel(
    id = termoMapWithMarkers.termoMap.id,
    name = termoMapWithMarkers.termoMap.name,
    description = termoMapWithMarkers.termoMap.description,
    markersAmountText = markersAmountText,
    iconResId = iconResId,
    markers = termoMapWithMarkers.markers,
  )
}

fun TermoMapUiModel.asModel(): TermoMapWithMarkers =
  TermoMapWithMarkers(
    termoMap = TermoMap(
      id = id,
      name = name,
      description = description,
    ),
    markers = markers,
  )
