package com.university.termomaps.features.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.termomaps.databinding.ItemMapBinding
import com.university.termomaps.features.list.model.TermoMapUiModel

class MapListAdapter(private val listener: OnMapClickListener) :
  ListAdapter<TermoMapUiModel, MapListAdapter.MapViewHolder>(TermoMapDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
    val binding = ItemMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MapViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun getItemCount(): Int = currentList.size

  inner class MapViewHolder(
    private val binding: ItemMapBinding,
  ) : RecyclerView.ViewHolder(binding.root) {

    init {
      binding.root.setOnClickListener {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
          val item = getItem(position)
          listener.onItemClick(item)
        }
      }

      binding.root.setOnLongClickListener {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
          val item = getItem(position)
          listener.onLongPressed(item, binding.root)
        }
        true
      }

      binding.ivShare.setOnClickListener {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
          val item = getItem(position)
          listener.onShareClick(item)
        }
      }
    }

    fun bind(uiModel: TermoMapUiModel) {
      with(binding) {
        ivMapAvatar.setImageResource(uiModel.iconResId)
        tvMapAvatarCount.text = uiModel.markersAmountText
        tvMapName.text = uiModel.name
        tvMapDescription.text = uiModel.description
      }
    }
  }

  class TermoMapDiffCallback : DiffUtil.ItemCallback<TermoMapUiModel>() {
    override fun areItemsTheSame(oldItem: TermoMapUiModel, newItem: TermoMapUiModel): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TermoMapUiModel, newItem: TermoMapUiModel): Boolean {
      return oldItem == newItem
    }
  }

  interface OnMapClickListener {
    fun onItemClick(mapUiModel: TermoMapUiModel)
    fun onShareClick(mapUiModel: TermoMapUiModel)
    fun onLongPressed(mapUiModel: TermoMapUiModel, view: View)
  }
}