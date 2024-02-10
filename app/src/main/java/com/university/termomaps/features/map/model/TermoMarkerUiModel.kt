package com.university.termomaps.features.map.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.university.termomaps.R
import com.university.termomaps.database.entity.TermoMarker

data class TermoMarkerUiModel(
  val id: Long,
  val name: String,
  val latitude: Double,
  val longitude: Double,
  val temperatureLoss: Int,
  @DrawableRes val iconResId: Int,
  val termoMapId: Int,
)

fun TermoMarker.asUiModel(): TermoMarkerUiModel {
  val iconResId: Int = when {
    temperatureLoss <= 20 -> R.drawable.ic_cold_marker
    else -> R.drawable.ic_warm_marker
  }
  return TermoMarkerUiModel(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    temperatureLoss = temperatureLoss,
    iconResId = iconResId,
    termoMapId = termoMapId,
  )
}

fun TermoMarkerUiModel.asModel(): TermoMarker =
  TermoMarker(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    temperatureLoss = temperatureLoss,
    termoMapId = termoMapId,
  )

fun TermoMarkerUiModel.toMarkerOptions(context: Context): MarkerOptions {
  return MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
    .snippet("Temperature loss: $temperatureLoss")
    .icon(getMarkerBitmapFromDrawableRes(context, iconResId))
    .alpha(0.9f)
    .draggable(false)
    .visible(true)
}

fun getMarkerBitmapFromDrawableRes(context: Context, @DrawableRes drawableRes: Int): BitmapDescriptor {
  val drawable = ContextCompat.getDrawable(context, drawableRes)
  return getMarkerBitmapFromDrawable(drawable)
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