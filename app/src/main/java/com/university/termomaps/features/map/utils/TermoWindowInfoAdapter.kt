package com.university.termomaps.features.map.utils

import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.university.termomaps.databinding.CustomInfoWindowBinding

class TermoWindowInfoAdapter(private val layoutInflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
  override fun getInfoWindow(marker: Marker): View? = null

  override fun getInfoContents(marker: Marker): View {
    val binding = CustomInfoWindowBinding.inflate(layoutInflater, null, false).apply {
      tvTitle.text = marker.title
      tvSnippet.text = marker.snippet
    }

    return binding.root
  }
}