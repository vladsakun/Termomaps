package com.university.termomaps.features.map.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class TermoClusterItem(
  private val position: LatLng,
  val termoMarker: TermoMarkerUiModel,
) : ClusterItem {

  override fun getPosition(): LatLng {
    return position
  }

  override fun getTitle(): String? = null
  override fun getSnippet(): String? = null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as TermoClusterItem

    return termoMarker.id == other.termoMarker.id
  }

  override fun hashCode(): Int {
    return termoMarker.id.hashCode()
  }
}