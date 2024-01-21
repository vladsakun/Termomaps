package com.university.termomaps.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.collectWhenStarted(fragment: Fragment, action: suspend (T) -> Unit) {
  flowWithLifecycle(fragment.lifecycle, Lifecycle.State.STARTED)
    .onEach(action)
    .launchIn(fragment.lifecycleScope)
}