package com.university.termomaps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.UpsertTermoMapWithMarkersUseCase
import com.university.termomaps.database.entity.TermoMapWithMarkers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val upsertTermoMapWithMarkersUseCase: UpsertTermoMapWithMarkersUseCase,
) : ViewModel() {

  fun importMap(json: String) {
    viewModelScope.launch {
      val termoMarkers: TermoMapWithMarkers = Json.decodeFromString(json)
      upsertTermoMapWithMarkersUseCase(termoMarkers)
    }
  }
}