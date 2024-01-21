package com.university.termomaps.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
  tableName = "termo_marker",
)
data class TermoMarker(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val latitude: Double,
  val longitude: Double,
  val temperatureLoss: Int,
)