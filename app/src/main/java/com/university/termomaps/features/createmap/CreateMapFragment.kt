package com.university.termomaps.features.createmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.databinding.FragmentCreateMapBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.showKeyboard
import com.university.termomaps.features.map.TermoMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMapFragment : BaseFragment<FragmentCreateMapBinding>() {

  companion object {
    fun newInstance() = CreateMapFragment()
  }

  private val viewModel: CreateMapViewModel by viewModels()

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentCreateMapBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.navigateEvent.collectWhenStarted(this) { mapId ->
      requireActivity().supportFragmentManager.popBackStack()

      replaceWithBackStack(TermoMapFragment.newInstance(mapId))
    }

    with(binding) {
      toolbar.setNavigationOnClickListener { back() }

      btnCreateMap.setOnClickListener {
        viewModel.createMap(
          tietMapName.text.toString(),
          tietMapDescription.text.toString()
        )
      }

      tietMapName.showKeyboard()
    }
  }
}