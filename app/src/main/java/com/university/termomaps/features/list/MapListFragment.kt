package com.university.termomaps.features.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.university.termomaps.R
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.common.BottomMarginItemDecoration
import com.university.termomaps.databinding.FragmentMapListBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.px
import com.university.termomaps.ext.shareMap
import com.university.termomaps.features.createmap.CreateMapFragment
import com.university.termomaps.features.list.adapter.MapListAdapter
import com.university.termomaps.features.list.model.TermoMapUiModel
import com.university.termomaps.features.list.model.asModel
import com.university.termomaps.features.map.TermoMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapListFragment : BaseFragment<FragmentMapListBinding>(), MapListAdapter.OnMapClickListener {

  companion object {
    private val BOTTOM_PADDING_PX = 88.px

    fun newInstance() = MapListFragment()
  }

  private val viewModel: MapListViewModel by viewModels()
  private val mapListAdapter by lazy { MapListAdapter(this) }

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentMapListBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(binding) {
      btnCreateMap.setOnClickListener {
        replaceWithBackStack(CreateMapFragment.newInstance())
      }
    }

    setupAdapter()
  }

  override fun onItemClick(mapUiModel: TermoMapUiModel) {
    replaceWithBackStack(TermoMapFragment.newInstance(mapUiModel.id))
  }

  override fun onShareClick(mapUiModel: TermoMapUiModel) {
    requireContext().shareMap(mapUiModel.asModel())
  }

  override fun onLongPressed(mapUiModel: TermoMapUiModel, view: View) {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.delete_map)
      .setNegativeButton(R.string.cancel) { dialog, _ ->
        dialog.dismiss()
      }
      .setPositiveButton(R.string.delete) { dialog, _ ->
        viewModel.deleteMap(mapUiModel.id)
        dialog.dismiss()
      }
      .create()
      .show()
  }

  private fun setupAdapter() {
    with(binding.rvMapList) {
      adapter = mapListAdapter
      layoutManager = LinearLayoutManager(requireContext())
      addItemDecoration(BottomMarginItemDecoration(BOTTOM_PADDING_PX))
    }

    viewModel.termoMapsWithMarkers.collectWhenStarted(this) { maps ->
      mapListAdapter.submitList(maps)
    }
  }
}