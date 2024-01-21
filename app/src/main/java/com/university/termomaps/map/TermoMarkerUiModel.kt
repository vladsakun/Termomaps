package com.university.termomaps.map

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.university.termomaps.R
import com.university.termomaps.database.TermoMarker

data class TermoMarkerUiModel(
  val id: Long,
  val name: String,
  val latitude: Double,
  val longitude: Double,
  val temperatureLoss: Int,
  @DrawableRes val iconResId: Int,
) {
  companion object {
    fun from(termoMarker: TermoMarker): TermoMarkerUiModel {
      val iconResId: Int = when {
        termoMarker.temperatureLoss <= 20 -> R.drawable.ic_cold_winter_thermometer
        else -> R.drawable.ic_hot_summer_thermometer
      }
      return TermoMarkerUiModel(
        id = termoMarker.id,
        name = termoMarker.name,
        latitude = termoMarker.latitude,
        longitude = termoMarker.longitude,
        temperatureLoss = termoMarker.temperatureLoss,
        iconResId = iconResId,
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

fun TermoMarkerUiModel.toMarkerOptions(context: Context): MarkerOptions {
  return MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
    .snippet("Temperature loss: $temperatureLoss")
    .icon(getMarkerBitmapFromDrawable(ContextCompat.getDrawable(context, iconResId)))
    .alpha(0.7f)
    .draggable(false)
    .visible(true)
}

fun getMarkerBitmapFromDrawable(drawable: Drawable?): BitmapDescriptor {
  requireNotNull(drawable)
  val bitmap = when (drawable) {
    is BitmapDrawable -> Bitmap.createBitmap(drawable.bitmap)
    is VectorDrawable -> Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    else -> throw IllegalArgumentException("unsupported drawable type")
  }

  val canvas = Canvas(bitmap)
  drawable.setBounds(0, 0, canvas.width, canvas.height)
  drawable.draw(canvas)
  return BitmapDescriptorFactory.fromBitmap(bitmap)
}