package com.university.termomaps.features.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.databinding.FragmentMapListBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.features.list.adapter.MapListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapListFragment : BaseFragment<FragmentMapListBinding>() {

  companion object {
    fun newInstance() = MapListFragment()
  }

  private val viewModel: MapListViewModel by viewModels()
  private val adapter by lazy { MapListAdapter() }

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentMapListBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(binding) {
      btnCreateMap.setOnClickListener {

      }
    }

    setupAdapter()
  }

  private fun setupAdapter() {
    binding.rvMapList.adapter = adapter

    viewModel.termoMapsWithMarkers.collectWhenStarted(this) { maps ->
      adapter.submitList(maps)
    }
  }
}