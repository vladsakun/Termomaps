package com.university.termomaps.features.map.utils

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.university.termomaps.features.map.model.TermoClusterItem
import com.university.termomaps.features.map.model.toMarkerOptions

class TermoClusterRenderer(
  private val context: Context,
  map: GoogleMap,
  clusterManager: ClusterManager<TermoClusterItem>,
) : DefaultClusterRenderer<TermoClusterItem>(context, map, clusterManager) {

  override fun onBeforeClusterItemRendered(item: TermoClusterItem, markerOptions: MarkerOptions) {
    val data = item.termoMarker.toMarkerOptions(context)
    markerOptions
      .title(data.title)
      .snippet(data.snippet)
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