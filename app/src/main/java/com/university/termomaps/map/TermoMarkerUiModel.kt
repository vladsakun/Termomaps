package com.university.termomaps.map

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.university.termomaps.database.TermoMarker
import kotlinx.serialization.Serializable

data class TermoMarkerUiModel(
  val id: Long,
  val name: String,
  val latitude: Double,
  val longitude: Double,
  val temperatureLoss: Int,
  val hue: Float,
) {
  companion object {
    fun from(termoMarker: TermoMarker): TermoMarkerUiModel {
      val hue: Float = when {
        termoMarker.temperatureLoss <= 20 -> BitmapDescriptorFactory.HUE_GREEN
        termoMarker.temperatureLoss <= 60 -> BitmapDescriptorFactory.HUE_YELLOW
        else -> BitmapDescriptorFactory.HUE_RED
      }
      return TermoMarkerUiModel(
        id = termoMarker.id,
        name = termoMarker.name,
        latitude = termoMarker.latitude,
        longitude = termoMarker.longitude,
        temperatureLoss = termoMarker.temperatureLoss,
        hue = hue,
      )
    }

    fun to(termoMarkerUiModel: TermoMarkerUiModel): TermoMarker =
      with(termoMarkerUiModel) {
        TermoMarker(
          id = id,
          name = name,
          latitude = latitude,
          longitude = longitude,
          temperatureLoss = temperatureLoss,
        )
      }
  }
}

fun TermoMarkerUiModel.toMarkerOptions(): MarkerOptions {
  // Change the color of MarkerOptions depending on the temperature loss
  return MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
    .snippet("Temperature loss: $temperatureLoss")
    .icon(BitmapDescriptorFactory.defaultMarker(hue))
    .alpha(0.7f)
    .draggable(false)
    .visible(true)
}