package com.university.termomaps.features.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.university.termomaps.R
import com.university.termomaps.base.BaseFragment
import com.university.termomaps.databinding.DialogAddMarkerBinding
import com.university.termomaps.databinding.FragmentTermoMapBinding
import com.university.termomaps.ext.collectWhenStarted
import com.university.termomaps.ext.shareMap
import com.university.termomaps.features.list.map.model.TermoClusterItem
import com.university.termomaps.features.map.model.asModel
import com.university.termomaps.features.map.model.toMarkerOptions
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

  private lateinit var mMap: GoogleMap
  private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }

  override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentTermoMapBinding.inflate(inflater, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    val clusterManager = ClusterManager<TermoClusterItem>(requireContext(), mMap)
    val customClusterRenderer = CustomClusterRenderer(requireContext(), mMap, clusterManager)
    clusterManager.renderer = customClusterRenderer

    mMap.setOnCameraIdleListener(clusterManager)

    viewModel.markers.collectWhenStarted(this) { markers ->
      mMap.clear()
      clusterManager.clearItems()

      markers.forEach { marker ->
        val clusterItem = TermoClusterItem(
          LatLng(marker.latitude, marker.longitude),
          marker.name,
          "Temperature loss: ${marker.temperatureLoss}",
          marker,
        )
        clusterManager.addItem(clusterItem)
      }

      clusterManager.cluster()
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
    val markers = viewModel.markers.value.map { it.asModel() }
    val json = Json.encodeToString(markers)
    requireContext().shareMap(json)
  }
}

class CustomClusterRenderer(
  private val context: Context,
  map: GoogleMap,
  clusterManager: ClusterManager<TermoClusterItem>,
) : DefaultClusterRenderer<TermoClusterItem>(context, map, clusterManager) {

  override fun onBeforeClusterItemRendered(item: TermoClusterItem, markerOptions: MarkerOptions) {
    val data = item.termoMarker.toMarkerOptions(context)
    markerOptions
      .icon(data.icon)
      .alpha(data.alpha)
  }

  override fun onClusterItemUpdated(clusterItem: TermoClusterItem, marker: Marker) {
    val data = clusterItem.termoMarker.toMarkerOptions(context)
    marker.setIcon(data.icon)
    marker.setAnchor(data.anchorU, data.anchorV)
  }

  override fun shouldRenderAsCluster(cluster: Cluster<TermoClusterItem>): Boolean = cluster.size > 1
}
