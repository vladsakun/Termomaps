package com.university.termomaps.features.list.map.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.university.termomaps.features.map.model.TermoMarkerUiModel

data class TermoClusterItem(
  private val position: LatLng,
  private val title: String,
  private val snippet: String,
  val termoMarker: TermoMarkerUiModel,
) : ClusterItem {
  override fun getPosition(): LatLng {
    return position
  }

  override fun getTitle(): String {
    return title
  }

  override fun getSnippet(): String {
    return snippet
  }
}