package com.university.termomaps.features.markerdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.TermoMarkerRepository
import com.university.termomaps.database.entity.marker.TermoMarker
import com.university.termomaps.ext.stateInWhileSubscribed
import com.university.termomaps.features.markerdetail.MarkerDetailFragment.Companion.KEY_MARKER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
  private val termoMarkerRepository: TermoMarkerRepository,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val markerId by lazy { savedStateHandle.get<Long>(KEY_MARKER_ID)!! }

  val marker: StateFlow<TermoMarker?> =
    termoMarkerRepository
      .getMarkerById(markerId)
      .stateInWhileSubscribed(viewModelScope, null)

  fun updateMarker(name: String, temperatureLose: Int) {
    viewModelScope.launch {
      termoMarkerRepository.updateMarkerDetails(
        id = markerId,
        name = name,
        temperatureLoss = temperatureLose,
      )
    }
  }

  fun deleteMarker() {
    viewModelScope.launch {
      termoMarkerRepository.delete(markerId)
    }
  }
}