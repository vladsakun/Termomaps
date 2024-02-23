package com.university.termomaps.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = "termo_map")
data class TermoMap(
  @PrimaryKey(autoGenerate = false)
  val id: Int = UUID.randomUUID().hashCode(),
  val name: String,
  val description: String,
)