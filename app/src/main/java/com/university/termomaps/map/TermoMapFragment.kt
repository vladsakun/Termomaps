package com.university.termomaps.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.university.termomaps.R
import com.university.termomaps.databinding.DialogAddMarkerBinding
import com.university.termomaps.databinding.FragmentTermoMapBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.shareMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

@AndroidEntryPoint
class TermoMapFragment : Fragment(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
  private val fusedLocationClient by lazy {
    LocationServices.getFusedLocationProviderClient(requireActivity())
  }
  private val viewModel: TermoMapViewModel by viewModels()

  private var _binding: FragmentTermoMapBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentTermoMapBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    binding.btnShare.setOnClickListener {
      shareMarkers()
    }

    binding.btnMyLocation.setOnClickListener {
      if (ContextCompat.checkSelfPermission(
          requireContext(),
          Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        onSuccessLocationPermissionGranted()
      } else {
        requestLocationPermission()
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun onSuccessLocationPermissionGranted() {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
      if (location != null) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
        mMap.isMyLocationEnabled = true
      }
    }
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    mMap.uiSettings.isMyLocationButtonEnabled = false
    val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
    mMap.setMapStyle(mapStyleOptions)

    viewModel.markers.collectWhenStarted(this) { markers ->
      mMap.clear()
      markers.forEach { marker ->
        mMap.addMarker(
          marker.toMarkerOptions(requireContext())
        )
      }
    }

    mMap.setOnMapLongClickListener { latLng ->
      showAddMarkerDialog(latLng)
    }
  }

  private fun requestLocationPermission() {
    ActivityCompat.requestPermissions(
      requireActivity(),
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      LOCATION_PERMISSION_REQUEST_CODE
    )
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
  ) {
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
        ) {
          onSuccessLocationPermissionGranted()
        }
      } else {
        // Permission denied. Disable the functionality that depends on this permission.
        // You can also show a dialog to the user explaining why the permission is necessary.
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun showAddMarkerDialog(latLng: LatLng) {
    val dialogView = DialogAddMarkerBinding.inflate(LayoutInflater.from(context)).apply {
      numberPicker.minValue = 0
      numberPicker.maxValue = 100
    }
    MaterialAlertDialogBuilder(requireContext())
      .setView(dialogView.root)
      .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
        dialog.dismiss()
      }
      .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
        val name = dialogView.etName.text.toString()
        val temperature = dialogView.numberPicker.value
        viewModel.addMarker(name, latLng, temperature)
        dialog.dismiss()
      }
      .create()
      .show()
  }

  private fun shareMarkers() {
    val markers = viewModel.markers.value.map { TermoMarkerUiModel.to(it) }
    val json = Json.encodeToString(markers)
    requireContext().shareMap(json)
  }
}
