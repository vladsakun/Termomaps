package com.university.termomaps.features.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.termomaps.database.entity.TermoMapWithMarkers
import com.university.termomaps.databinding.ItemMapBinding

class MapListAdapter : ListAdapter<TermoMapWithMarkers, MapListAdapter.MapViewHolder>(TermoMapDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
    val binding = ItemMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MapViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun getItemCount(): Int {
    return currentList.size
  }

  inner class MapViewHolder(private val binding: ItemMapBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(termoMapWithMarkers: TermoMapWithMarkers) {
      with(binding) {
        tvMapName.text = termoMapWithMarkers.termoMap.name
        tvMapDescription.text = termoMapWithMarkers.termoMap.description
      }
    }
  }

  class TermoMapDiffCallback : DiffUtil.ItemCallback<TermoMapWithMarkers>() {
    override fun areItemsTheSame(oldItem: TermoMapWithMarkers, newItem: TermoMapWithMarkers): Boolean {
      return oldItem.termoMap.id == newItem.termoMap.id
    }

    override fun areContentsTheSame(oldItem: TermoMapWithMarkers, newItem: TermoMapWithMarkers): Boolean {
      return oldItem == newItem
    }
  }
}