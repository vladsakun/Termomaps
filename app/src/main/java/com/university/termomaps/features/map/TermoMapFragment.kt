package com.university.termomaps.features.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.clustering.ClusterManager
import com.university.termomaps.R
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.databinding.DialogAddMarkerBinding
import com.university.termomaps.databinding.FragmentTermoMapBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.defaultSetup
import com.university.termomaps.ext.shareMap
import com.university.termomaps.ext.showKeyboard
import com.university.termomaps.features.map.model.TermoClusterItem
import com.university.termomaps.features.map.utils.TermoClusterRenderer
import com.university.termomaps.features.map.utils.TermoWindowInfoAdapter
import com.university.termomaps.features.markerdetail.MarkerDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

@AndroidEntryPoint
class TermoMapFragment : BaseFragment<FragmentTermoMapBinding>(), OnMapReadyCallback {

  companion object {
    const val KEY_TERMO_MAP_ID = "key_termo_map_id"

    fun newInstance(termoMapId: Int) =
      TermoMapFragment().apply {
        arguments = Bundle().apply {
          putInt(KEY_TERMO_MAP_ID, termoMapId)
        }
      }
  }

  private val viewModel: TermoMapViewModel by viewModels()

  private var mMap: GoogleMap? = null
  private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }

  private val locationRequest by lazy {
    LocationRequest.create().apply {
      fastestInterval = 500
      priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }
  }
  private val locationCallback by lazy {
    object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        locationResult ?: return
        for (location in locationResult.locations) {
          if (location != null) {
            val currentLatLng = LatLng(location.latitude, location.longitude)
            showMyLocation(currentLatLng)
          }
        }
      }
    }
  }

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentTermoMapBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    // Ініціалізуємо карту
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    binding.btnShare.setOnClickListener { shareMarkers() }

    onSuccessLocationPermissionGranted()

    // Слухач події кліку на кнопку "Моя локація"
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

  override fun onMapReady(googleMap: GoogleMap) {
    // Коли карта завершила ініціалізацію, додаємо маркери
    mMap = googleMap

    val clusterManager = ClusterManager<TermoClusterItem>(requireContext(), mMap).apply {
      renderer = TermoClusterRenderer(requireContext(), checkNotNull(mMap), this)
    }

    mMap?.run {
      // Стиль карти
      uiSettings.isMyLocationButtonEnabled = false
      uiSettings.isCompassEnabled = false
      setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

      // Слухач події довгого кліку на карті
      setOnMapLongClickListener(::showAddMarkerDialog)
      // Слухач події завершення переміщення камери
      setOnCameraIdleListener(clusterManager)

      clear()
    }

    viewModel.markers.collectWhenStarted(this) { markers ->
      clusterManager.clearItems()

      // Додаємо маркери на карту
      markers.forEach { marker ->
        val clusterItem = TermoClusterItem(
          position = LatLng(marker.latitude, marker.longitude),
          termoMarker = marker,
        )

        // Додаємо маркер в кластер
        clusterManager.addItem(clusterItem)
      }

      // Кластеризуємо маркери
      clusterManager.cluster()
    }

    // Встановлюємо слухач події кліку на маркер
    with(clusterManager.markerCollection) {

      // Встановлюємо адаптер для вікна інформації
      setInfoWindowAdapter(TermoWindowInfoAdapter(LayoutInflater.from(requireContext())))

      // Встановлюємо слухач події кліку на вікно інформації
      setOnInfoWindowClickListener(::handleInfoWindowClick)
    }
  }

  // Відкриваємо діалог додавання маркера
  private fun showAddMarkerDialog(latLng: LatLng) {
    val dialogView = DialogAddMarkerBinding.inflate(LayoutInflater.from(context)).apply {
      numberPicker.defaultSetup()
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

    dialogView.etName.showKeyboard()
  }

  // Поділитися маркерами
  private fun shareMarkers() {
    val map = viewModel.termoMap.value ?: return
    val json = Json.encodeToString(map)
    requireContext().shareMap(json, mapName = map.termoMap.name)
  }

  private fun handleInfoWindowClick(marker: Marker) {
    val markerModel = viewModel.getMarker(marker.position) ?: return
    replaceWithBackStack(MarkerDetailFragment.newInstance(markerModel.id))
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

  override fun onPause() {
    super.onPause()
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

  @SuppressLint("MissingPermission")
  private fun onSuccessLocationPermissionGranted() {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
      if (location != null) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        showMyLocation(currentLatLng)
      } else {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun showMyLocation(currentLatLng: LatLng) {
    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
    mMap?.isMyLocationEnabled = true
  }
}