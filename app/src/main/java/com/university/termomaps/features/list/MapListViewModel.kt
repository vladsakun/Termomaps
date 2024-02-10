package com.university.termomaps.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.TermoMapRepository
import com.university.termomaps.database.entity.TermoMapWithMarkers
import com.university.termomaps.ext.stateInWhileSubscribedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MapListViewModel @Inject constructor(
  private val termoMapRepository: TermoMapRepository,
) : ViewModel() {

  val termoMapsWithMarkers: StateFlow<List<TermoMapWithMarkers>> =
    termoMapRepository.termoMapsWithMarkers
      .stateInWhileSubscribedList(viewModelScope)
}