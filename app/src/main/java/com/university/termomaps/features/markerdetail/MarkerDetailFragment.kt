package com.university.termomaps.features.markerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.university.termomaps.R
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.database.entity.marker.TermoMarker
import com.university.termomaps.databinding.FragmentMarkerDetailsBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.defaultSetup
import com.university.termomaps.ext.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailFragment : BaseFragment<FragmentMarkerDetailsBinding>() {

  private val viewModel: MarkerDetailsViewModel by viewModels()

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentMarkerDetailsBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(binding) {
      setupToolbar()

      btnSave.setOnClickListener {
        viewModel.updateMarker(
          etName.text.toString(),
          npTemperatureLoss.value,
        )
        back()
      }
    }

    viewModel.marker.collectWhenStarted(this, ::setupUi)
  }

  private fun setupToolbar() {
    with(binding.toolbar) {
      setNavigationOnClickListener { back() }
      setOnMenuItemClickListener(::handleMenuItemClick)
    }
  }

  private fun handleMenuItemClick(menuItem: MenuItem) = when (menuItem.itemId) {
    R.id.action_delete -> {
      showDeleteMarkerDialog()
      true
    }

    else -> false
  }

  private fun setupUi(marker: TermoMarker?) {
    marker ?: return

    with(binding) {
      toolbar.title = marker.name
      etName.setText(marker.name)

      npTemperatureLoss.defaultSetup()
      npTemperatureLoss.wrapSelectorWheel = true
      npTemperatureLoss.value = marker.temperatureLoss

      etName.showKeyboard()
      etName.setSelection(marker.name.length)
    }
  }

  private fun showDeleteMarkerDialog() {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.delete_marker)
      .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
        dialog.dismiss()
      }
      .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
        viewModel.deleteMarker()
        dialog.dismiss()
        back()
      }
      .create()
      .show()
  }

  companion object {
    const val KEY_MARKER_ID = "key_marker_id"

    fun newInstance(markerId: Long) = MarkerDetailFragment().apply {
      arguments = Bundle().apply {
        putLong(KEY_MARKER_ID, markerId)
      }
    }
  }
}