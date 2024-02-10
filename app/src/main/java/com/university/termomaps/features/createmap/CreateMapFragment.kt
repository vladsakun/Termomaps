package com.university.termomaps.features.createmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.university.termomaps.R
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.databinding.FragmentCreateMapBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.features.map.TermoMapFragment

class CreateMapFragment : BaseFragment<FragmentCreateMapBinding>() {

  private val viewModel: CreateMapViewModel by viewModels()

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentCreateMapBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.navigateEvent.collectWhenStarted(this) { mapId ->
      requireActivity()
        .supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container, TermoMapFragment.newInstance(mapId))
        .commit()
    }

    with(binding) {
      tbCreateMap.setNavigationOnClickListener {
        requireActivity().supportFragmentManager.popBackStack()
      }

      btnCreateMap.setOnClickListener {
        viewModel.createMap(
          tietMapName.text.toString(),
          tietMapDescription.text.toString()
        )
      }
    }
  }
}