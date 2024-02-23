package com.university.termomaps.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.TermoMapRepository
import com.university.termomaps.ext.stateInWhileSubscribedList
import com.university.termomaps.features.list.model.TermoMapUiModel
import com.university.termomaps.features.list.model.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapListViewModel @Inject constructor(
  private val termoMapRepository: TermoMapRepository,
) : ViewModel() {

  val termoMapsWithMarkers: StateFlow<List<TermoMapUiModel>> =
    termoMapRepository.termoMapsWithMarkers
      .map { it.map(::asUiModel) }
      .stateInWhileSubscribedList(viewModelScope)

  fun deleteMap(id: Int) {
    viewModelScope.launch {
      termoMapRepository.deleteMap(id)
    }
  }
}