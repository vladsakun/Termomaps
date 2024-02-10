package com.university.termomaps.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T> Flow<T>.collectWhenStarted(fragment: Fragment, action: suspend (T) -> Unit) {
  flowWithLifecycle(fragment.lifecycle, Lifecycle.State.STARTED)
    .onEach(action)
    .launchIn(fragment.lifecycleScope)
}

fun <T> Flow<T>.stateInWhileSubscribed(
  scope: CoroutineScope,
  initialValue: T,
  stopTimeoutMillis: Long = 5_000L,
): StateFlow<T> = stateIn(scope, SharingStarted.WhileSubscribed(stopTimeoutMillis), initialValue)

fun <T> Flow<List<T>>.stateInWhileSubscribedList(
  scope: CoroutineScope,
  stopTimeoutMillis: Long = 5_000L,
): StateFlow<List<T>> = stateIn(scope, SharingStarted.WhileSubscribed(stopTimeoutMillis), emptyList())
