package com.university.termomaps.features.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.university.termomaps.data.TermoMapRepository
import com.university.termomaps.data.TermoMarkerRepository
import com.university.termomaps.database.entity.TermoMarker
import com.university.termomaps.ext.stateInWhileSubscribedList
import com.university.termomaps.features.map.model.TermoMarkerUiModel
import com.university.termomaps.features.map.model.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermoMapViewModel @Inject constructor(
  private val termoMapRepository: TermoMapRepository,
  private val savedStateHandle: SavedStateHandle,
  private val termoMarkerRepository: TermoMarkerRepository,
) : ViewModel() {

  private val termoMapId by lazy { getMapId() }

  val markers: StateFlow<List<TermoMarkerUiModel>> =
    termoMapRepository.getTermoMapWithMarkers(termoMapId)
      .map { map ->
        map.markers.map { it.asUiModel() }
      }
      .stateInWhileSubscribedList(viewModelScope)

  fun addMarker(name: String, latLng: LatLng, temperatureLoss: Int) {
    viewModelScope.launch {
      val termoMarket = TermoMarker(
        name = name,
        latitude = latLng.latitude,
        longitude = latLng.longitude,
        temperatureLoss = temperatureLoss,
        termoMapId = termoMapId,
      )

      termoMarkerRepository.upsert(termoMarket)
    }
  }

  private fun getMapId(): Int {
    return checkNotNull(savedStateHandle.get<Int>(TermoMapFragment.KEY_TERMO_MAP_ID))
  }
}