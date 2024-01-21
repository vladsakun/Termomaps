package com.university.termomaps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.TermoMarkerRepository
import com.university.termomaps.database.TermoMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val termoMarkerRepository: TermoMarkerRepository,
) : ViewModel() {

  fun importMarkers(json: String) {
    viewModelScope.launch {
      val termoMarkers: List<TermoMarker> = Json.decodeFromString(json)
      termoMarkerRepository.insert(termoMarkers)
    }
  }
}