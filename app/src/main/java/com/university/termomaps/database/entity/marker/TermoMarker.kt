package com.university.termomaps.database.entity.marker

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.university.termomaps.database.entity.TermoMap
import kotlinx.serialization.Serializable

/**
 * Об'єкт маркера на карті
 *
 * @property id - унікальний ідентифікатор
 * @property name - назва маркера
 * @property latitude - широта
 * @property longitude - довгота
 * @property temperatureLoss - втрата температури
 * @property termoMapId - ідентифікатор термокарти
 */
@Serializable
/**
 * Таблиця маркерів
 */
@Entity(
  tableName = "termo_marker",
  foreignKeys = [
    ForeignKey(
      entity = TermoMap::class,
      parentColumns = ["id"],
      childColumns = ["termoMapId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ]
)
data class TermoMarker(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val latitude: Double,
  val longitude: Double,
  val temperatureLoss: Int,
  val termoMapId: Int,
)