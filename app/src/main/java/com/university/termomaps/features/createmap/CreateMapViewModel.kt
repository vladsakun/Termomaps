package com.university.termomaps.features.createmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.termomaps.data.TermoMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMapViewModel @Inject constructor(
  private val termoMapRepository: TermoMapRepository,
) : ViewModel() {

  private val navigateChannel = Channel<Int>(Channel.CONFLATED)
  val navigateEvent: Flow<Int> = navigateChannel.receiveAsFlow()

  // Todo remove hardcoded author
  fun createMap(name: String, description: String, author: String = "KyivBud") {
    viewModelScope.launch {
      val newMapId = termoMapRepository.createMap(name, description, author)
      navigateChannel.trySend(newMapId)
    }
  }
}