package com.university.termomaps.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.university.termomaps.database.TermoMarker
import com.university.termomaps.database.TermoMarkerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermoMapViewModel @Inject constructor(
  private val termoMarketDao: TermoMarkerDao,
) : ViewModel() {

  val markers: StateFlow<List<TermoMarkerUiModel>> =
    termoMarketDao
      .getAll()
      .map { markers -> markers.map { TermoMarkerUiModel.from(it) } }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

  fun addMarker(name: String, latLng: LatLng, temperatureLoss: Int) {
    val termoMarket = TermoMarker(
      name = name,
      latitude = latLng.latitude,
      longitude = latLng.longitude,
      temperatureLoss = temperatureLoss,
    )
    viewModelScope.launch {
      termoMarketDao.upsert(termoMarket)
    }
  }
}